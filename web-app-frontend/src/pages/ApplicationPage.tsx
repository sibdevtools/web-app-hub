import React from 'react';
import { useApplicationContext } from '../contexts/ApplicationContext';
import { Loader } from '../components/Loader';
import { Alert } from 'react-bootstrap';

export const ApplicationPage: React.FC = () => {
  const { configuration, loading, error } = useApplicationContext();

  if (loading) {
    return <Loader />;
  }

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  if (!configuration) {
    return <Alert variant="danger">No configs available</Alert>;
  }

  const currentPath = window.location.pathname;

  let iframeSrc: string | null = null;
  for (const [key, value] of Object.entries(configuration.configs)) {
    const prefix = `/apps/${key}`;
    const frontendUrl = value.frontendUrl;
    if (currentPath === prefix) {
      iframeSrc = frontendUrl;
      break;
    } else if (currentPath.startsWith(`${prefix}/`)) {
      const suffix = currentPath.substring(prefix.length + 1);
      if (frontendUrl.endsWith('/')) {
        iframeSrc = frontendUrl + suffix;
      } else {
        iframeSrc = `${frontendUrl}/${suffix}`;
      }
      break;
    }
  }

  if (!iframeSrc) {
    return <Alert variant="danger">No matching configuration found for the current URL</Alert>;
  }

  return (
    <iframe src={`${iframeSrc}${window.location.search}`}
            className={'w-100 vh-100'}
            title="Application display"
    />
  );
};

