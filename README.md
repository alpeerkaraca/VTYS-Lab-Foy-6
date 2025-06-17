# Foy6 Uygulaması

Bu proje, Spring Boot kullanılarak geliştirilmiş bir web uygulamasıdır. Temel amacı, Redis, MongoDB ve Hazelcast gibi farklı NoSQL veritabanlarından öğrenci verilerini okuma performansını karşılaştırmaktır. Uygulama başlangıçta belirtilen sayıda öğrenci verisini bu üç veritabanına da yükler ve ardından bu verileri getirmek için API endpoint'leri sunar.

## Proje Teknolojileri
- **Java Sürümü:** 21
- **Framework:** Spring Boot 3.5.0
- **Veritabanları:**
    - Redis
    - MongoDB
    - Hazelcast
- **Ön Yüz (Template Engine):** Thymeleaf
- **Build Aracı:** Maven

## Uygulama Gereksinimleri
- JDK 21
- Maven 3.6 veya üzeri
- `siege` (Benchmark script'inin tüm özelliklerini kullanmak için)

## Kurulum

### 1. Manuel Veritabanı Kurulumu

Proje, Redis, MongoDB ve Hazelcast veritabanlarına ihtiyaç duyar. Bu servisleri kendi web sitelerinden indirip manuel olarak kurmanız gerekmektedir.

1.  **Redis:**
    -   Redis'i [resmi web sitesinden](https://redis.io/download) indirin ve kurun.
    -   Redis sunucusunu varsayılan port olan `6379` üzerinden başlatın.

2.  **MongoDB:**
    -   MongoDB'yi [resmi web sitesinden](https://www.mongodb.com/try/download/community) indirin ve kurun.
    -   MongoDB sunucusunu varsayılan port olan `27017` üzerinden başlatın.

3.  **Hazelcast:**
    -   Hazelcast'i [resmi web sitesinden](https://hazelcast.com/) indirin ve kurun.
    -   Hazelcast sunucusunu başlatın. **Önemli:** Sunucuyu yapılandırırken `cluster-name` özelliğinin `foy6-cluster` olarak ayarlandığından emin olun. Bu, uygulamanın veritabanına doğru şekilde bağlanabilmesi için gereklidir.

### 2. Uygulama Yapılandırması

Proje içerisindeki `src/main/resources/application.properties` dosyası, veritabanı bağlantı bilgilerini içerir. Varsayılan ayarlar `localhost` üzerindeki standart portları kullanır. Eğer veritabanlarınız farklı bir adres veya port üzerinde çalışıyorsa, bu dosyayı düzenlemeniz gerekmektedir.

## Çalıştırma

1. Projeyi klonlayın ve dizine geçiş yapın.
    ```bash
    git clone https://github.com/alpeerkaraca/VTYS-Lab-Foy-6
    cd VTYS-Lab-Foy-6
    ```
2.  Projeyi klonladıktan sonra, Maven bağımlılıklarını yükleyin:
    ```bash
    ./mvnw clean install
    ```
   
3.  Uygulamayı başlatın:
    ```bash
    ./mvnw spring-boot:run
    ```
   

Uygulama başlatıldığında, `DataInitializer` sınıfı devreye girerek 10,000 adet öğrenci kaydı oluşturur ve bu kayıtları Redis, MongoDB ve Hazelcast veritabanlarına yükler.

## Endpointler

Uygulama, hem bir web arayüzü hem de RESTful API endpoint'leri sunar.

### Web Arayüzü

-   `GET /`: Öğrenci aramak için bir web formu sunan ana sayfayı döndürür. Kullanıcı, öğrenci numarasını girip Redis, MongoDB veya Hazelcast kaynaklarından birini seçerek arama yapabilir.

### REST API (StudentController)

Bu endpoint'ler, belirtilen öğrenci numarasına (`studentNo`) göre öğrenci verisini getirir.

-   **Redis'ten Öğrenci Getir:**
    -   `GET /nosql-lab/rd/{studentNo}`

-   **MongoDB'den Öğrenci Getir:**
    -   `GET /nosql-lab/mon/{studentNo}`

-   **Hazelcast'ten Öğrenci Getir:**
    -   `GET /nosql-lab/hz/{studentNo}`

## Performans Testi (Benchmark)

Proje, `benchmark.sh` adında bir betik içerir. Bu betik, üç veritabanı endpoint'inin performansını `curl` ve `siege` araçlarını kullanarak test eder.

Testi çalıştırmak için:
```bash
./benchmark.sh
```

Betik, aşağıdaki işlemleri gerçekleştirir:
-   Her bir endpoint'e (Redis, MongoDB, Hazelcast) paralel ve sıralı istekler gönderir.
-   Test sonuçlarını `assets/results/` dizini altına `.results` uzantılı dosyalar olarak kaydeder.

## Proje Yapısı

```
.
├── benchmark.sh
├── pom.xml
├── README.MD
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── foy6
    │   │           └── foy6
    │   │               ├── Foy6Application.java
    │   │               ├── Config
    │   │               │   ├── DataInitializer.java
    │   │               │   ├── HazelcastConfig.java
    │   │               │   └── RedisConfig.java
    │   │               ├── Controllers
    │   │               │   ├── HomeController.java
    │   │               │   └── StudentController.java
    │   │               └── Models
    │   │                   └── Student.java
    │   └── resources
    │       ├── application.properties
    │       └── templates
    │           └── index.html
    └── test
        └── java
            └── com
                └── foy6
                    └── foy6
                        └── Foy6ApplicationTests.java
```