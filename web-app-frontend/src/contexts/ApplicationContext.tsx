import React, { createContext, useContext, useState, useEffect, useMemo } from 'react';
import axios from 'axios';

type WebApplicationType = {
  code: string;
  frontendUrl: string;
  icon: string;
  title: string;
  description: string;
  healthStatus: string;
  version: string;
};

type GetConfigurationRsType = {
  configs: {
    [key: string]: WebApplicationType;
  };
};

interface ApplicationContextType {
  configuration: GetConfigurationRsType | null;
  loading: boolean;
  error: string | null;
}

const ApplicationContext = createContext<ApplicationContextType | undefined>(undefined);

export const useApplicationContext = () => {
  const context = useContext(ApplicationContext);
  if (context === undefined) {
    throw new Error('useApplicationContext must be used within a ApplicationProvider');
  }
  return context;
};

export const ApplicationProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [rs, setRs] = useState<GetConfigurationRsType | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchConfigurations = async () => {
    try {
      const response = await axios.get<GetConfigurationRsType>('/api/web/app/hub/v1/configuration/');
      setRs(response.data);
    } catch (err) {
      setError('Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchConfigurations();
  }, []);

  const providerValue = useMemo(
    () => ({ configuration: rs, loading, error }),
    [rs, loading, error]
  );

  return (
    <ApplicationContext.Provider value={providerValue}>
      {children}
    </ApplicationContext.Provider>
  );
};
