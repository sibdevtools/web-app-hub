import React from 'react';
import { useApplicationContext } from '../contexts/ApplicationContext';

export const ApplicationDisplay: React.FC = () => {
  const { configuration, loading, error } = useApplicationContext();

  if (loading) {
    return <div>Loading iframe...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!configuration) {
    return <div>No configs available</div>;
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
    return <div>No matching configuration found for the current URL</div>;
  }

  return (
    <iframe src={`${iframeSrc}${window.location.search}`}
            style={{
              height: '100vh',
              width: '100%',
            }}
            title="Application display"
    />
  );
};

