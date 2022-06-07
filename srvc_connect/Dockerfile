FROM confluentinc/cp-kafka-connect:latest

# Load confluent kafka connect image and add s3 connector to plugin
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-s3:latest

# Create a connector with our configuration
ADD connector_config.json /connector_config.json
CMD /etc/confluent/docker/run & (sleep 20 && curl -X POST -H "Content-Type: application/json" --data @/connector_config.json http://localhost:8083/connectors && sleep infinity)
