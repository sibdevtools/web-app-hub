import React from 'react';
import { useApplicationContext } from '../contexts/ApplicationContext';

const ApplicationCards: React.FC = () => {
  const { configuration, loading, error } = useApplicationContext();

  if (loading) {
    return <div>Loading applications...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  const configs = configuration?.configs;
  if (!configs || Object.keys(configs).length === 0) {
    return <div>No applications available.</div>;
  }

  return (
    <div className="container mt-4">
      <div className="row">
        {Object.entries(configs).map(([key, value]) => (
          <div className="col-md-4" key={key}>
            <div className="card mb-4">
              <div className="card-header">
                <h5 className="card-title">{key}</h5>
              </div>
              <div className="card-body">
                <p className="card-text">Application Path: {value}</p>
                <a href={`/apps/${key}`} className="btn btn-primary" target="_blank" rel="noopener noreferrer">
                  Open Application
                </a>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ApplicationCards;
