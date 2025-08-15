import React from 'react';
import { useApplicationContext, WebApplicationType } from '../contexts/ApplicationContext';
import { Alert, Badge, Card, CardText, Col, Container, Row } from 'react-bootstrap';
import { Variant } from 'react-bootstrap/types';
import { Loader } from '@sibdevtools/frontend-common';

function chunkArray<T>(array: T[], size: number): T[][] {
  const result = [];
  for (let i = 0; i < array.length; i += size) {
    result.push(array.slice(i, i + size));
  }
  return result;
}

export const ApplicationsPage: React.FC = () => {
  const { configuration, loading, error } = useApplicationContext();

  if (loading) {
    return <Loader loading={loading} />;
  }

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  const configs = configuration?.configs;
  if (!configs || Object.keys(configs).length === 0) {
    return <Alert variant="info">No applications available.</Alert>;
  }

  const codeComparator = (a: [string, WebApplicationType], b: [string, WebApplicationType]) => {
    return a[0].localeCompare(b[0], undefined, { sensitivity: 'base' });
  }

  const sortedConfigs = Object.entries(configs).sort(codeComparator);

  const getStyleForHealth = (healthStatus: string): Variant => {
    if (healthStatus === 'UP') {
      return 'success'
    }
    if (healthStatus === 'WAITING') {
      return 'warning'
    }
    if (healthStatus === 'DOWN') {
      return 'danger'
    }
    return 'secondary'
  };

  const chunked = chunkArray(sortedConfigs, 3)

  return (
    <Container className="mt-4 overflow-y-auto">
      {chunked.map(sub => (
        <Row>
          {sub.map(([key, value]) => (
            <Col md={4} key={key}>
              <Card className="mb-4">
                <Card.Header>
                  <Card.Title>
                    <Row>
                      <Col xs={8} sm={8} lg={9} xl={10}>
                        <a href={`/apps/${key}`}>
                          {value.title || value.code}
                        </a>
                      </Col>
                      <Col xs={4} sm={4} lg={3} xl={2} className={'text-end'}>
                        <small>
                          <Badge
                            pill
                            bg={getStyleForHealth(value.healthStatus)}
                          >
                            <span>{value.healthStatus}</span>
                          </Badge>
                        </small>
                      </Col>
                    </Row>
                  </Card.Title>
                </Card.Header>
                <Card.Body>
                  <CardText>{value.description}</CardText>
                </Card.Body>
                {value.version && (
                  <Card.Footer>
                    <Row>
                      <Col xs={6}>
                        <small className={'text-muted'}>Version:</small>
                      </Col>
                      <Col xs={6} className={'text-end'}>
                        <small className={'text-muted'}>{value.version}</small>
                      </Col>
                    </Row>
                  </Card.Footer>
                )}
              </Card>
            </Col>
          ))}
        </Row>
      ))}
    </Container>
  );
};
