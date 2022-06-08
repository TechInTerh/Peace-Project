FROM confluentinc/cp-kafka-connect:7.1.0

# Load confluent kafka connect image and add s3 connector to plugin
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-s3:10.0.8

# Create a connector with our configuration
ADD connector_config.json /connector_config.json
CMD /etc/confluent/docker/run & (until (curl -X POST -H "Content-Type: application/json" --data @/connector_config.json http://kafka-connect:8083/connectors) do echo '...waiting...' && sleep 5; done;) && sleep infinity
