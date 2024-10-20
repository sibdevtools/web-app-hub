import React, { useEffect, useRef, useState } from 'react';
import { useApplicationContext } from '../contexts/ApplicationContext';
import { Loader } from '../components/Loader';
import { Alert } from 'react-bootstrap';

export const ApplicationPage: React.FC = () => {
  const { configuration, loading, error } = useApplicationContext();
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const [iframeSrc, setIframeSrc] = useState<string | null>(null);
  const [appPrefix, setAppPrefix] = useState<string | null>(null);

  useEffect(() => {
    if (!configuration) return;

    const currentPath = window.location.pathname;
    let matchedSrc: string | null = null;
    let matchedPrefix: string | null = null;

    for (const [key, value] of Object.entries(configuration.configs)) {
      const prefix = `/apps/${key}`;
      const frontendUrl = value.frontendUrl;

      if (currentPath === prefix) {
        matchedSrc = frontendUrl;
        matchedPrefix = prefix;
        break;
      } else if (currentPath.startsWith(`${prefix}/`)) {
        const suffix = currentPath.substring(prefix.length + 1);
        matchedSrc = frontendUrl.endsWith('/')
          ? frontendUrl + suffix
          : `${frontendUrl}/${suffix}`;
        matchedPrefix = prefix;
        break;
      }
    }

    if (matchedSrc && matchedPrefix) {
      setIframeSrc(matchedSrc + window.location.search);
      setAppPrefix(matchedPrefix);
    }
  }, [configuration]);

  useEffect(() => {
    const iframe = iframeRef.current;
    if (!iframe || !appPrefix || !iframeSrc) return;

    const handleIframeNavigation = () => {
      try {
        const iframeWindow = iframe.contentWindow;
        if (!iframeWindow) return;

        const iframeUrl = new URL(iframeWindow.location.href);
        const iframePath = iframeUrl.pathname;

        const mappedPath = iframePath.replace(iframeSrc, '');

        const newUrl = `${appPrefix}${mappedPath}${iframeUrl.search}`;

        if (window.location.pathname + window.location.search !== newUrl) {
          history.pushState(null, '', newUrl);
        }
      } catch (error) {
        console.warn('Unable to access iframe URL due to cross-origin restrictions', error);
      }
    };

    iframe.addEventListener('load', handleIframeNavigation);

    return () => {
      iframe.removeEventListener('load', handleIframeNavigation);
    };
  }, [iframeSrc, appPrefix]);

  if (loading) {
    return <Loader />;
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
