import React from 'react';
import { useApplicationContext } from '../contexts/ApplicationContext';
import { LogoutSquare01Icon } from 'hugeicons-react';

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

  const getStyleForHealth = (healthStatus: string) => {
    if (healthStatus === 'UP') {
      return 'bg-success border-light'
    }
    if (healthStatus === 'WAITING') {
      return 'bg-warning border-light'
    }
    if (healthStatus === 'DOWN') {
      return 'bg-danger border-light'
    }

    return 'bg-secondary border-light'
  };

  return (
    <div className="container mt-4">
      <div className="row">
        {Object.entries(configs).map(([key, value]) => (
          <div className="col-md-4" key={key}>
            <div className="card mb-4">
              <div className="card-header">
                <h5 className="card-title">
                  <div
                    className={`position-absolute top-0 start-100 translate-middle p-2 border badge rounded-pill ${getStyleForHealth(value.healthStatus)}`}>
                    <span>{value.healthStatus}</span>
                  </div>
                  {value.title || value.code}
                </h5>
              </div>
              <div className="card-body">
                <p className="card-text">{value.description}</p>
              </div>
              <div className="card-footer">
                <a href={`/apps/${key}`} className="btn btn-primary float-end">
                  <LogoutSquare01Icon />
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
