# PubSubToCloudStorageBeamStream
Streaming data from PubSub to Firestore using Apache Beam

java -jar target/pubsubToFirestoreBeamStream-bundled-1.1.jar --inputTopicSubscription="projects/mkloud/subscriptions/pubSubOutputStreamSubscription" --firestoreProject="projects/mkloud/" --firestoreCollection="documents/stream/" --jobName="StreamJobPubSubToFirestore" --runner=DataflowRunner --gcpTempLocation="gs://mhk-dataflow-bucket/PubSubToFirestoreTemp" --region=northamerica-northeast1 --zone=northamerica-northeast1-b


