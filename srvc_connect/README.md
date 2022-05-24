# Setup connect between kafka and minio

## Start minio
docker run -p 9000:9000 -p 9001:9001 \
minio/minio server /data --console-address ":9001"

%% for more info see: hub.docker.com/r/minio/minio/

## Install kafka
https://www.apache.org/dyn/closer.cgi?path=/kafka/3.2.0/kafka_2.13-3.2.0.tgz
tar -xzf kafka_2.13-3.2.0.tgz
cd kafka

From now on we do everything from the kafka folder

## Start kafka with a topic named test
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
bin/kafka-topics.sh --create --topic test --bootstrap-server localhost:9092

%% for more info see: kafka.apache.org/quickstart


## Add connect plugin for s3
mkdir confluent-plugins && cd confluent-plugins
wget https://api.hub.confluent.io/api/plugins/confluentinc/kafka-connect-s3/versions/5.2.0/archive
unzip archive
cd .. && mkdir -p plugins/kafka-connect-s3
mv confluent-plugins/confluentinc-kafka-connect-s3-5.2.0/lib/* plugins/kafka-connect-s3/

## Add configuration for connector
mkdir ~/.aws && cd ~/.aws
touch credentials

*credentials*
[default]
aws_access_key_id = minioadmin
aws_secret_access_key = minioadmin

add s3-sink.properties and connector.properties into kafka/plugins

## Start Kafka connector
./bin/connect-standalone.sh ../plugins/connector.properties ../plugins/s3-sink.properties

## Common problems

### Failed to find any class that implements Connector
plugin.path in connector.properties is wrong, should point to plugins folder

### Unable to load AWS credentials from any provider
https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html

### Multipart upload failed to complete
Maybe there is an error in s3 credentials
