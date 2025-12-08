wget https://client.hub.confluent.io/confluent-hub-client-latest.tar.gz
tar -xzf confluent-hub-client-latest.tar.gz
sudo mv confluent-hub-client /usr/local/bin/

confluent-hub install --help

cd /home/ec2-user/dockerdir/cdc

confluent-hub install confluentinc/kafka-connect-s3:latest \
  --component-dir /home/ec2-user/dockerdir/cdc/connect-plugins \
  --worker-configs /home/ec2-user/dockerdir/cdc/worker.properties \
  --no-prompt