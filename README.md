# PubSubToCloudStorageBeamStream
Streaming data from PubSub to Firestore using Apache Beam

java -jar target/pubsubToFirestoreBeamStream-bundled-1.1.jar --inputTopicSubscription="projects/mkloud/subscriptions/pubSubOutputStreamSubscription" --firestoreProject="projects/mkloud/" --firestoreCollection="documents/stream/" --jobName="StreamJobPubSubToFirestore" --runner=DataflowRunner --gcpTempLocation="gs://mhk-dataflow-bucket/PubSubToFirestoreTemp" --region=northamerica-northeast1 --zone=northamerica-northeast1-b

gcloud dataflow flex-template build gs://mhk-dataflow-bucket/dataflow/templates/pubsub-to-Firestore-stream-flex-template.json \
--image-gcr-path "northamerica-northeast1-docker.pkg.dev/mkloud/mkloud-artifacts/dataflow/pubsub-to-firestore:latest" \
--sdk-language "JAVA" \
--flex-template-base-image JAVA11 \
--metadata-file "metadata.json" \
--jar "target/pubsubToFirestoreBeamStream-bundled-1.1.jar" \
--env FLEX_TEMPLATE_JAVA_MAIN_CLASS="com.kloudbuddy.stream.App"

gcloud dataflow flex-template build gs://mhk-dataflow-bucket-1/dataflow/templates/pubsub-to-Firestore-stream-flex-template.json \
--image-gcr-path "northamerica-northeast1-docker.pkg.dev/mkloud/mkloud-artifacts/dataflow/pubsub-to-firestore:latest" \
--sdk-language "JAVA" \
--flex-template-base-image JAVA11 \
--jar "target/pubsubToFirestoreBeamStream-bundled-1.1.jar" \
--env FLEX_TEMPLATE_JAVA_MAIN_CLASS="com.kloudbuddy.stream.App"


gcloud dataflow flex-template run "firestore-to-pubsub-`date +%Y%m%d-%H%M%S`" --template-file-gcs-location="gs://mhk-dataflow-bucket-1/dataflow/templates/pubsub-to-Firestore-stream-flex-template.json" --parameters inputTopicSubscription="projects/mkloud/subscriptions/pubSubOutputStreamSubscription" --parameters firestoreProject="projects/mkloud/" --parameters firestoreCollection="documents/stream/" --enable-streaming-engine --service-account-email="dataflow-sa@mkloud.iam.gserviceaccount.com" --staging-location="gs://mhk-dataflow-bucket-1/staging/" --temp-location="gs://mhk-dataflow-bucket-1/tempLocation/" --region="us-central1" 
