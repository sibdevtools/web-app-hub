import React, { useEffect, useRef, useState } from 'react';
import { useApplicationContext } from '../contexts/ApplicationContext';
import { Alert } from 'react-bootstrap';
import { Loader } from '@sibdevtools/frontend-common';

export const ApplicationPage: React.FC = () => {
  const { configuration, loading, error } = useApplicationContext();
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const [iframeSrc, setIframeSrc] = useState<string | null>(null);
  const [iframePrefix, setIframePrefix] = useState<string | null>(null);
  const [appPrefix, setAppPrefix] = useState<string | null>(null);

  useEffect(() => {
    if (!configuration) return;

    const currentPath = window.location.pathname;

    for (const [key, value] of Object.entries(configuration.configs)) {
      const prefix = `/apps/${key}`;
      const frontendUrl = value.frontendUrl;

      if (currentPath === prefix) {
        setIframePrefix(frontendUrl);
        setIframeSrc(frontendUrl + window.location.search);
        setAppPrefix(prefix);
        break;
      } else if (currentPath.startsWith(`${prefix}/`)) {
        const suffix = currentPath.substring(prefix.length + 1);
        const matchedSrc = frontendUrl.endsWith('/')
          ? frontendUrl + suffix
          : `${frontendUrl}/${suffix}`;
        setIframePrefix(frontendUrl);
        setIframeSrc(matchedSrc + window.location.search);
        setAppPrefix(prefix);
        break;
      }
    }
  }, [configuration]);

  useEffect(() => {
    const iframe = iframeRef.current;
    if (!iframe || !appPrefix || !iframeSrc || !iframePrefix) return;

    const handleIframeNavigation = () => {
      const iframeDocument = iframe.contentDocument || iframe?.contentWindow?.document;
      if (!iframeDocument) {
        return;
      }

      const observer = new MutationObserver(function () {
        const documentURI = iframeDocument.documentURI;
        const parts = documentURI.split(iframePrefix)
        if (parts.length !== 2) {
          return
        }
        const path = `${appPrefix}/${parts[parts.length - 1]}`
        if (window.location.pathname + window.location.search !== path) {
          history.pushState(null, '', path);
        }
      });

      const config = {
        childList: true,
        attributes: true,
        subtree: true,
        characterData: true
      };

      observer.observe(iframeDocument.body, config);
    };

    iframe.addEventListener('load', handleIframeNavigation);

    return () => {
      iframe.removeEventListener('load', handleIframeNavigation);
    };
  }, [iframeSrc, appPrefix]);

  if (loading) {
    return <Loader loading={loading} />;
  }

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  if (!configuration) {
    return <Alert variant="danger">No configs available</Alert>;
  }

  if (!iframeSrc) {
    return <Alert variant="danger">No matching configuration found for the current URL</Alert>;
  }

  return (
    <iframe
      ref={iframeRef}
      src={iframeSrc}
      className={'w-100 vh-100'}
      title="Application display"
    />
  );
};
