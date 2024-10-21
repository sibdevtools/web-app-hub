import React from 'react';
import { useApplicationContext } from '../contexts/ApplicationContext';
import { LogoutSquare01Icon } from 'hugeicons-react';
import { Loader } from '../components/Loader';
import {
  Alert, Badge, Button,
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  CardText,
  CardTitle,
  Col,
  Container,
  Row
} from 'react-bootstrap';
import { Variant } from 'react-bootstrap/types';

export const ApplicationsPage: React.FC = () => {
  const { configuration, loading, error } = useApplicationContext();

  if (loading) {
    return <Loader />;
  }

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  const configs = configuration?.configs;
  if (!configs || Object.keys(configs).length === 0) {
    return <Alert variant="info">No applications available.</Alert>;
  }

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

  return (
    <Container className="mt-4">
      <Row>
        {Object.entries(configs).map(([key, value]) => (
          <Col md={4} key={key}>
            <Card className="mb-4">
              <Card.Header>
                <Card.Title>
                  <Badge
                    pill
                    bg={getStyleForHealth(value.healthStatus)}
                    className={`position-absolute top-0 start-100 translate-middle p-2 border`}>
                    <span>{value.healthStatus}</span>
                  </Badge>
                  <a href={`/apps/${key}`}>
                    {value.title || value.code}
                  </a>
                </Card.Title>
              </Card.Header>
              <Card.Body>
                <CardText>{value.description}</CardText>
              </Card.Body>
              {value.version && (
                <Card.Footer>
                  <small className={'text-muted'}>Version: {value.version}</small>
                </Card.Footer>
              )}
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};
