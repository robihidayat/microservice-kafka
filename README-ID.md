Microservice Kafka Sample
==================

Project ini adalah simple penggunaan kafka untuk komunikasi diantara microservices. 

Project dibuat dengan Docker container.

Ada empat service yang digunakan dalam microservices: 
- Order untuk membuat pesanan. Services ini akan mengirimkan pesan ke kafka, dengan menggunakan `KafkaTemplate`.
- Shipment menerima pesanan dan di ekstrak informasi yang dibutuhkan untuk mengirim barang. 
- Invoicing menerima message juga, tapi semua informasi ditampilkan.
- Email menerima message juga, sebagai notify kepada customer yang meng-order. 

Message yang diterima oleh Shipment, Invoicing, Email menggunakan `@KafkaListener`. 
Penggunakan case kali ini menggunakan topic order dengan lima partisi. 
Shipment, invoicing, dan email mempunyai consumer grup yang berbeda. jadi beberapa instance 
shipment, invoicing dan email dapat berjalan. setaiap instance dapat mengambil spesific events. 

 
Teknilogi
------------

- Spring Boot
- Spring Kafka
- Apache httpd
- Kafka
- Zookeeper
- Postgres
- Docker Compose to link the containers.

Bagaimana untuk memulai ?
----------

Lihat [Bagaimana cara untuk memulai](HOW-TO-RUN-ID.md) untuk lebih detail.

Pertama harus men-deploy aplikasi order, kemudian setelah itu invoice, shipment
email yang harus di-deploy. 

Keteranagn code
-------------------

Microservices: 
- [microservice-kafka-order](microservice-kafka/microservice-kafka-order) untuk membuat order
- [microserivce-kafka-shipping](microservice-kafka/microservice-kafka-shipping) untuk membuat pengiriman
- [microservice-kafka-invoicing](microservice-kafka/microservice-kafka-invoicing) untuk membuat faktur
- [microservice-kafka-email](microservice-kafka/microservice-kafka-email) untuk mengirim email

Data pesanan disalin - termasuk data pelanggan dan barang-barangnya. 
Jadi jika pelanggan atau barang berubah dalam sistem pesanan
ini tidak berlaku untuk pengiriman dan faktur yang ada. Itu akan terjadi
aneh jika perubahan harga akan mengubah faktur yang ada. jadi
hanya informasi yang dibutuhkan untuk pengiriman dan fakturnya
disalin ke sistem lain.

Order microservices menggunakan `KafkaTemplate` dari Spring untuk mengirim pesan
sementara tiga microservices yang lain menggunakan annotation `@KafkaListener` 
untuk dapat menerima pesan jika ada pesan baru masuk ke kafka. Semua pessan disimpan
dengan menggunakan topic `order`, dengan menggunakan lima partisi yang mendukung scalability.


untuk testing, digunakan embedded Kafka. annotation `@ClassRule` digunakan untuk memulai, 
dan annotation `@BeforeClass` digunakan untuk mengkonfigure spring kafka agar bisa menggunakan embeded kafka server. 

Order JSON Serialize. 
Jadi objek `Order` di serialize dengan structure data JSON.  
microservices yang lain membaca data untuk shipping, invoicing, email. microservices invoice membaca sebagai 
obejct `Invoice` dan microservice shipping membaca sebagai object `Delivery`. 
`Order` terdapat semua data untuk `Invoice` begitu juga `Delivery`.
JSON serialization flexibel dalam hal serializer. saat order deserialized menjadi  `Invoice` dan `Delivery` data tambahan akan di hiraukan.

Ada empat Docker container untuk microservices.
kemudian beberapa Docker container untuk Apache httpd, Kafka, Zookeeper and Postgres.



Riquest yang datang di handle oleh Apache httpd server. bisa menggunakan port 8080 sebagai Docker host
contoh <http://localhost:8080>. HTTP request


Incoming http request are handled by the Apache httpd server. It is
available at port 8080 of the Docker host
e.g. <http://localhost:8080>.  HTTP requests are forwarded to the
microservices. Kafka is used for the communication between the
microservices. Kafka needs Zookeeper to coordinate instances. Postgres
is used by all microservices to store data. Each microservices uses
its own database in the Postgres instance so they are decoupled in
that regard.

You can scale the listener with e.g. `docker-compose scale
shipping=2`. The logs (`docker logs
mskafka_shipping_1`) will show which partitions the instances listen
to and which records they handle.

You can also start a shell on the Kafka server `docker exec -it
mskafka_kafka_1 /bin/sh` and then take a look at the records in the
topic using `kafka-console-consumer.sh --bootstrap-server kafka:9092
--topic order --from-beginning`.
