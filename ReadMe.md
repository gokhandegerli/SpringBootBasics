# Project is structured on DDD basically

## order domain: it uses CompletableFuture*  
## greeting domain: basic example for decorator pattern

### 8 core 100 OS thread varsa, OS 8 thread’i aynı anda başlatıyor. Her bir thread’e işlem süresi veriyor, süresi bitince pause edip diğer thread’e izin veriyor

İşte konuyu basit ve detaylı bir şekilde açıklıyorum:

Temel Kavramlar:
CPU Core (Çekirdek):
Fiziksel bir işlemci çekirdeği, aynı anda tek bir işlem yapabilir (örneğin, 8 çekirdek = 8 işlem aynı anda).

OS Thread (İş Parçacığı):
İşletim sistemi tarafından yönetilen sanal iş birimidir. Uygulamalar, aynı anda birden fazla iş yapmak için thread'ler oluşturur (örneğin, 100 thread = 100 farklı iş).

Zamanlama (Scheduling):
İşletim sistemi, tüm thread'leri adil bir şekilde çalıştırmak için zaman dilimleri (time slices) kullanır.

Senaryo: 8 Çekirdek & 100 Thread
Fiziksel Sınır: 8 çekirdek aynı anda en fazla 8 thread çalıştırabilir.

100 Thread Nasıl Çalışır?
İşletim sistemi, thread'leri hızlıca değiştirerek (context switching) hepsinin çalıştığı izlenimini verir.

Adım Adım Nasıl Çalışır?
İlk 8 Thread Başlatılır:
İşletim sistemi, 8 çekirdeğe 8 thread atar. Her çekirdek, kendi thread'ini çalıştırır.
Örnek:

Thread 1 → Çekirdek 1

Thread 2 → Çekirdek 2

...

Thread 8 → Çekirdek 8

Zaman Dilimi (Time Slice) Sınırı:
Her thread'e belirli bir süre verilir (örneğin, 10ms). Süre dolunca, thread duraklatılır (pause).

Context Switching (Bağlam Değişimi):
Duraklatılan thread'in durumu kaydedilir ve çekirdek, sıradaki thread'i çalıştırmaya başlar.
Örnek:

Çekirdek 1, Thread 1'i duraklatır → Thread 9'u çalıştırır.

Çekirdek 2, Thread 2'yi duraklatır → Thread 10'u çalıştırır.

...

Tüm Thread'ler Dönüşümlü Çalışır:
Bu döngü, tüm 100 thread tamamlanana kadar devam eder.
Görselleştirme:

Copy
Çekirdek 1: [Thread1] → [Thread9] → [Thread17] → ...  
Çekirdek 2: [Thread2] → [Thread10] → [Thread18] → ...  
...  
Neden Bu Kadar Hızlı Hissedilir?
Saniyede Binlerce İşlem:
Örneğin, 10ms'lik zaman dilimlerinde bir çekirdek saniyede 100 thread değiştirebilir.

8 çekirdek → Saniyede 800 thread işleme kapasitesi.

Parallelism (Aynı Anda) vs. Concurrency (Dönüşümlü):

Parallelism: 8 thread aynı anda çalışır (gerçek paralellik).

Concurrency: 100 thread dönüşümlü çalışır (hızlı geçiş yanılsaması).

Örnek Senaryo:
Uygulama: Bir web sunucusu 100 kullanıcıya hizmet veriyor.

Çekirdekler: 8 çekirdek.

İşleyiş:

Her kullanıcı isteği bir thread olarak oluşturulur.

İşletim sistemi, 8 çekirdeği kullanarak tüm istekleri dönüşümlü işler.

Kullanıcılar, tüm işlemlerin aynı anda yapıldığını düşünür.

Önemli Notlar:
Thread Sayısı Artarsa:
Çok fazla thread (örneğin 1000) performansı düşürebilir, çünkü context switching maliyeti artar.

Optimizasyon:
Uygulamalar, thread havuzları (thread pools) kullanarak thread sayısını optimize eder.

Hyper-Threading:
Bazı CPU'lar bir çekirdekte 2 thread çalıştırabilir (8 çekirdek → 16 thread), ancak bu fiziksel çekirdek sayısını değiştirmez.

Sonuç:
8 çekirdekli bir CPU, 100 thread'i aynı anda çalıştıramaz, ancak nanosecond seviyesinde hızlı geçişlerle tüm thread'leri sırayla işler. Bu, kullanıcıya "aynı anda" çalışıyormuş gibi hissettirir. 🚀

### bu makineye t aninda 100 request geldi, ilk 8 request vok uzun surdu, 9. request nasil manage ediliyor?

İşte detaylı ve basitleştirilmiş açıklama:

Senaryo:
Makine: 8 çekirdekli CPU, 100 işletim sistemi (OS) thread'i destekliyor.

Durum: Aynı anda (t anında) 100 request (istek) geliyor.

Sorun: İlk 8 request çok uzun sürüyor (CPU-bound veya bloklayıcı işlemler).

Soru: 9. request nasıl işleniyor?

Adım Adım İşleyiş:
1. İlk 8 Request İşlenmeye Başlar
   Fiziksel Çekirdekler: 8 çekirdek olduğu için, OS ilk 8 thread'i bu çekirdeklere atar.
   Örnek:

Thread 1 → Çekirdek 1

Thread 2 → Çekirdek 2

...

Thread 8 → Çekirdek 8

Uzun Süren İşlemler:
İlk 8 thread, CPU'yu meşgul eden işlemler yapıyorsa (örneğin matematiksel hesaplamalar), CPU-bound olarak kabul edilir. Bu durumda, thread'ler CPU'yu bırakmaz (kendi isteğiyle yield etmez).

2. 9. Request'in Durumu
      OS Scheduler (Zamanlayıcı):
      İşletim sistemi, tüm thread'lerin adil şekilde çalışmasını sağlamak için time slicing (zaman dilimleme) kullanır.

Her thread'e belirli bir süre verilir (örneğin 10ms).

Süre dolunca, OS o thread'i duraklatır (preemption) ve sıradaki thread'i çalıştırır.

9. Request'in Kuyruğa Alınması:

9. request'in thread'i (Thread 9), Ready Queue (hazır kuyruğu) adı verilen bir bekleme listesine alınır.

OS scheduler, çekirdeklerden birinin boşalmasını (veya zaman diliminin dolmasını) bekler.

3. Context Switching ile 9. Request İşlenir
   Zaman Dilimi Dolunca:
   Örneğin, Çekirdek 1'deki Thread 1'in zaman dilimi (10ms) dolduğunda:

Thread 1 duraklatılır ve durumu kaydedilir.

OS, Ready Queue'den Thread 9'u alıp Çekirdek 1'e atar.

Thread 9 işlenmeye başlar.

Sonraki Adım:

Thread 1, tekrar sıra geldiğinde kaldığı yerden devam eder.

Diğer çekirdeklerde de aynı işlem tekrarlanır (örneğin Çekirdek 2'deki Thread 2'nin süresi dolunca Thread 10 çalışır).

Örnek Zaman Çizelgesi:
Zaman (ms)	Çekirdek 1	Çekirdek 2	...	Çekirdek 8
0-10	Thread 1	Thread 2	...	Thread 8
10-20	Thread 9	Thread 2	...	Thread 8
20-30	Thread 1	Thread 10	...	Thread 8
...	...	...	...	...
Önemli Noktalar:
Preemptive Scheduling:
OS, thread'leri zorla duraklatabilir (CPU'yu ele geçirmelerini engeller).

CPU-Bound vs. I/O-Bound:

CPU-Bound: Thread CPU'yu sürekli meşgul ediyorsa (örneğin döngüsel hesaplamalar), zaman dilimi dolana kadar çalışır.

I/O-Bound: Thread disk/network gibi işlemlerde bekliyorsa, OS hemen başka thread'e geçer.

Prioritization:
Bazı OS'ler öncelikli thread'lere daha fazla zaman dilimi verebilir (örneğin gerçek zamanlı sistemler).

9. Request Ne Kadar Bekler?
   Gecikme Süresi:

8 çekirdek varsa, 9. request en kötü durumda ~10ms bekler (ilk zaman dilimi sonunda bir çekirdek boşalır).

Gerçekte, thread'ler genellikle I/O-bound olduğu için daha erken çalışmaya başlar.

Örnek:
Thread 1, 5ms sonra bir dosya okuma işlemi yaparsa (I/O-bound), OS hemen Thread 9'u çalıştırır.

Sonuç:
OS, tüm thread'leri adil şekilde yönetir.

9. request, ilk 8 thread'in zaman dilimi dolduğunda veya I/O işlemine geçtiğinde işlenir.

Performans Sorunu:
8 çekirdekte 100 thread çalıştırmak, yüksek context switching maliyeti nedeniyle performansı düşürebilir. Bu durumda async I/O veya thread pool kullanmak daha verimli olur.

### buradan core'dan anladigimiz cpu degeri degil mi?

Evet, doğru anladınız! "Core" terimi, bu bağlamda CPU çekirdeği anlamına gelir. İşte detaylı açıklama:

Core (Çekirdek) Nedir?
Fiziksel Bir Birimdir:
CPU'nun içindeki bağımsız işlem birimidir. Her çekirdek, aynı anda tek bir iş parçacığını (thread) işleyebilir.

Örneğin: 8 çekirdekli bir CPU, aynı anda 8 thread çalıştırabilir.

Core vs. Thread
Özellik	Core (Çekirdek)	Thread (İş Parçacığı)
Fiziksel/Yapay	Fiziksel donanım birimi	Yazılımsal sanal birim
Aynı Anda İşlem	1 thread	1 işlem (OS tarafından zamanlanır)
Hyper-Threading	1 core, 2 thread destekleyebilir*	Sanal çoğaltma ile performans artışı
*Hyper-Threading teknolojisiyle bir çekirdek, 2 thread'i paralelmiş gibi yönetebilir, ancak bu fiziksel çekirdek sayısını değiştirmez.

Örnek Senaryo: 8 Core & 100 Thread
Fiziksel Sınır: 8 core → Aynı anda 8 thread çalıştırılabilir.

OS Yönetimi:

İşletim sistemi, 100 thread'i zaman dilimleri (time slices) ile dönüşümlü olarak çalıştırır.

Örneğin: Her thread'e 10ms çalışma süresi verilir.

Context Switching:

Bir thread'in durumu kaydedilir, diğer thread çalıştırılır.

Bu geçişler o kadar hızlıdır ki, kullanıcıya tüm thread'ler aynı anda çalışıyormuş gibi gelir.

Core'un CPU ile İlişkisi
CPU = Central Processing Unit (Merkezi İşlem Birimi):

Bir CPU, birden fazla core içerebilir (örneğin: 8 çekirdekli CPU).

Çekirdek sayısı arttıkça, paralel işlem kapasitesi artar.

Örnek Kullanım:

Oyunlar, video render gibi ağır işlerde çok çekirdekli CPU'lar daha performanslıdır.

Teknik Terim Karışıklığı
"Core" Başka Anlamlarda da Kullanılabilir:

Yazılımda: Bir framework'ün temel modülü (örneğin: .NET Core).

Donanımda: GPU çekirdeği, AI çekirdeği gibi farklı birimler.

Ancak bu bağlamda:
Core = CPU çekirdeği olarak ele alınmalıdır.

Sonuç
Evet, bu bağlamda "core" CPU çekirdeği anlamına gelir.

8 çekirdekli bir CPU, aynı anda 8 thread işleyebilir. 100 thread ise OS tarafından zaman paylaşımlı olarak yönetilir.

🚀 Performans İpucu: Uygulamalarınızı optimize etmek için çok çekirdekli mimarileri destekleyen kod yazın (paralel programlama, async/await vb.).

### Peki RAM burada ne ise yariyo? Paralel islem kapasitesi RAM'e mi bagli, core/cpu sayisina mi?

Elbette! RAM (Random Access Memory) ve CPU çekirdekleri, bilgisayar performansında farklı roller oynar. İşte detaylı açıklama:

RAM'in Rolü:
Geçici Bellek (Working Memory):

RAM, CPU'nun işlem yaparken kullandığı geçici verileri depolar (örneğin: açık uygulamalar, dosyalar, işlenmemiş veriler).

CPU, verileri doğrudan RAM'den okur ve işler.

Örnek: Bir Excel dosyasını açtığınızda, dosya RAM'de tutulur. CPU bu veriler üzerinde hesaplama yapar.

Çoklu İşlem Kapasitesi:

Aynı anda ne kadar çok uygulama veya thread çalıştırılıyorsa, o kadar fazla RAM gerekir.

Yetersiz RAM: Uygulamalar yavaşlar veya "swap" alanı (disk üzerinde sanal bellek) kullanılır → Performans düşer.

Paralel İşlemlerde RAM'in Etkisi:

Veri Boyutu: İşlenecek veri seti çok büyükse (örneğin 3D render), yüksek RAM kapasitesi kritiktir.

Veri Hızı: DDR4/DDR5 RAM'ler, daha yüksek bant genişliği sağlayarak CPU'nun veriye hızlı erişmesini sağlar.

Paralel İşlem Kapasitesi Neye Bağlı?
CPU Çekirdek Sayısı:

Ana Belirleyici: Paralel işlem kapasitesi, CPU'daki çekirdek sayısıyla doğrudan ilişkilidir.

Örneğin: 8 çekirdek → Aynı anda 8 thread çalıştırabilir.

Hyper-Threading: Bir çekirdek 2 thread'i paralelmiş gibi yönetebilir, ancak fiziksel çekirdek kadar etkili değildir.

RAM'in Rolü:

Destekleyici Faktör: RAM, CPU'nun veriye hızlı erişmesini sağlar.

Yetersiz RAM Durumu:

CPU, veri beklerken boşta kalır → Paralel işlemler yavaşlar.

Örnek: 8 çekirdekli bir CPU, 4GB RAM ile 100 thread'i yönetemez (RAM tükenir, disk swap başlar).

Örnek Senaryolar:
1. Video Düzenleme (CPU + RAM Bağımlı):
   CPU: 16 çekirdek → Çoklu frame'leri paralel render eder.

RAM: 32GB+ → Büyük video dosyalarını anlık işler.

Sonuç: Hem çok çekirdek hem yüksek RAM gereklidir.

2. Web Sunucusu (CPU Ağırlıklı):
   CPU: 8 çekirdek → Aynı anda 8 kullanıcıya hizmet verir.

RAM: 16GB → Her kullanıcı için küçük veri parçaları yeterli.

Sonuç: CPU çekirdek sayısı önceliklidir.

3. Büyük Veri Analizi (RAM Ağırlıklı):
   CPU: 16 çekirdek → Paralel hesaplama yapar.

RAM: 64GB+ → Tüm veri seti RAM'de tutulur.

Sonuç: RAM yetersizse CPU beklemeye geçer, performans düşer.

Özet Tablo:
Bileşen	Paralel İşlemdeki Rolü	Yetersizlik Belirtisi
CPU Çekirdek	Aynı anda çalışan thread sayısını belirler.	CPU kullanımı %100'e yaklaşır.
RAM	İşlenen verilerin hızlı erişilebilirliğini sağlar.	Disk swap artar, uygulamalar takılır.
Hangi Durumda Hangi Bileşen Önemli?
CPU Çekirdek:

Paralel hesaplama gerektiren işler (render, kod derleme, simülasyon).

Çoklu uygulamalar aynı anda çalıştırıldığında.

RAM:

Büyük veri setleriyle çalışırken (veritabanları, grafik işleme).

Sanal makineler veya container'lar kullanıldığında.

Sonuç:
Paralel işlem kapasitesi için CPU çekirdek sayısı kritiktir.

RAM, bu kapasitenin verimli kullanılmasını sağlayan bir destekçidir.

İdeal Denge:

8 çekirdekli CPU + 16-32GB RAM → Çoğu senaryo için yeterli.

16 çekirdek + 64GB RAM → Profesyonel iş yükleri için.

🚀 Not: SSD diskler, RAM yetersiz kaldığında "swap" alanını hızlandırarak performansı artırabilir, ancak RAM'in yerini tutmaz!

### Core Java'da Async islem denince ne anlamamiz gerekiyor?

Core Java'da Asenkron (Async) İşlem, bir işin ana thread'i (örneğin, kullanıcı arayüzü veya ana uygulama akışı) bloklamadan, arka planda başlatılması ve sonucunun işlenmesidir. Bu, performansı artırır ve kaynakları verimli kullanmayı sağlar. İşte detaylı açıklama:

Temel Kavramlar
Senkron İşlem (Synchronous):

Kod satır satır çalışır, bir işlem bitmeden diğeri başlamaz.

Örnek: Bir dosya okuma işlemi tamamlanana kadar sonraki kod bekler.

Asenkron İşlem (Asynchronous):

İşlem arka planda başlatılır, ana thread bloklanmaz.

Örnek: Bir REST API çağrısı yapılır, cevap beklenmeden diğer işlemlere devam edilir.

Core Java'da Asenkron Yöntemler
Java'nın temel kütüphaneleriyle asenkron işlemler için kullanılan yapılar:

1. Thread'ler (Temel Seviye)
   Thread Sınıfı: İş parçacıkları oluşturarak asenkron çalışma sağlar.

java
Copy
new Thread(() -> {
// Uzun süren işlem (örneğin, dosya indirme)
System.out.println("Arka planda çalışıyor...");
}).start();
Avantaj: Basit ve doğrudan kontrol.

Dezavantaj: Thread yönetimi zor, kaynak tüketimi yüksek.

2. ExecutorService (Daha Kontrollü)
   Thread havuzu (Thread Pool) kullanarak kaynakları optimize eder.

java
Copy
ExecutorService executor = Executors.newFixedThreadPool(2);
executor.submit(() -> {
System.out.println("Task 1 çalışıyor...");
});
executor.submit(() -> {
System.out.println("Task 2 çalışıyor...");
});
executor.shutdown();
3. Future ve Callable (Sonuç Bekleme)
   Asenkron işlemin sonucunu beklemek için kullanılır.

java
Copy
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(() -> {
Thread.sleep(2000);
return "İşlem tamamlandı!";
});

// Sonuç beklenebilir (bloklayıcı):
String result = future.get();
System.out.println(result);
4. CompletableFuture (Java 8+)
   Zincirleme asenkron işlemler ve callback'ler için ideal.

java
Copy
CompletableFuture.supplyAsync(() -> "Merhaba")
.thenApplyAsync(s -> s + " Dünya")
.thenAcceptAsync(System.out::println); // "Merhaba Dünya"
Avantaj: Non-blocking, esnek, fonksiyonel programlama dostu.

Neden Asenkron İşlem?
Performans Artışı: CPU veya I/O kaynaklı beklemelerde ana thread bloklanmaz.

Responsive UI: Kullanıcı arayüzü donmaz (GUI uygulamalarında kritik).

Ölçeklenebilirlik: Aynı anda birden fazla işlem yönetilebilir.

Örnek Senaryo: REST API Çağrısı
java
Copy
CompletableFuture.supplyAsync(() -> {
// Uzun süren API çağrısı
return httpClient.get("https://api.example.com/data");
}).thenAcceptAsync(response -> {
// Cevap geldiğinde UI'ı güncelle
updateUI(response);
}).exceptionally(ex -> {
// Hata yönetimi
System.err.println("Hata: " + ex.getMessage());
return null;
});
Dikkat Edilmesi Gerekenler
Thread Güvenliği: Paylaşılan kaynaklara erişimde senkronizasyon önemli.

Kaynak Yönetimi: Thread havuzu boyutu ve yaşam döngüsü kontrol edilmeli.

Callback Hell: CompletableFuture ile aşırı iç içe yapılar okunabilirliği düşürür.

Popüler Kütüphaneler
Project Loom (Java 19+): Virtual Thread'lerle hafif asenkron işlemler.

Reactive Streams (Java 9+): Flow API ile reaktif programlama.

Özet:
Core Java'da asenkron işlemler, thread'ler, ExecutorService, Future, ve CompletableFuture ile yönetilir. Doğru kullanıldığında uygulama performansını ve kullanıcı deneyimini önemli ölçüde artırır. 🚀

### Thread'ler, ExecutorService, Future, ve CompletableFuture bunlarin arasindaki fark nedir? What? Why? How? olarak aciklar misin?

Java'da asenkron işlemler için kullanılan Thread'ler, ExecutorService, Future, ve CompletableFuture arasındaki farkları What? Why? How? başlıklarıyla açıklayalım:

1. Thread'ler
   What? (Ne?)
   Temel İş Parçacığı Yönetimi: Java'da en temel asenkron işlem birimi. Her Thread, bağımsız bir iş parçacığını temsil eder.

Manuel Yönetim: Thread'lerin başlatılması, durdurulması ve senkronizasyonu manuel olarak yapılır.

Why? (Neden Kullanılır?)
Basit Senaryolar: Küçük ölçekli veya tek seferlik asenkron işlemler için.

Doğrudan Kontrol: Thread yaşam döngüsü üzerinde tam kontrol sağlar.

How? (Nasıl Kullanılır?)
java
Copy
// Thread örneği
new Thread(() -> {
System.out.println("Thread çalışıyor...");
}).start();
Dezavantajlar:

Yüksek Maliyet: Her Thread için OS kaynağı tüketilir.

Zor Yönetim: Çok sayıda Thread oluşturmak performansı düşürür.

2. ExecutorService
   What? (Ne?)
   Thread Havuzu (Pool): Thread'leri yeniden kullanarak kaynak tüketimini optimize eden bir framework.

Task Tabanlı: İşler (Runnable veya Callable), Thread havuzuna gönderilir.

Why? (Neden Kullanılır?)
Kaynak Optimizasyonu: Sabit sayıda Thread ile çoklu iş yönetimi.

Ölçeklenebilirlik: Büyük ölçekli uygulamalarda performans artışı sağlar.

How? (Nasıl Kullanılır?)
java
Copy
ExecutorService executor = Executors.newFixedThreadPool(4); // 4 Thread'lik havuz
executor.submit(() -> {
System.out.println("Task çalışıyor...");
});
executor.shutdown(); // Havuzu kapat
Avantajlar:

Thread yeniden kullanımı ve otomatik yönetim.

3. Future
   What? (Ne?)
   Asenkron Sonuç Taşıyıcı: Bir işlemin sonucunu temsil eden bir nesne.

Bloklayıcı (Blocking): Sonuç alınana kadar beklemeyi zorunlu kılar.

Why? (Neden Kullanılır?)
Sonuç Bekleme: Asenkron işlemin tamamlanmasını ve sonucunu almayı sağlar.

How? (Nasıl Kullanılır?)
java
Copy
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(() -> {
Thread.sleep(2000);
return "Sonuç";
});

// Bloklayıcı bekleyiş:
String result = future.get(); // 2 saniye bekler
System.out.println(result);
Dezavantajlar:

Bloklayıcı: get() metodu iş parçacığını bloklar.

4. CompletableFuture
   What? (Ne?)
   Non-Blocking ve Zincirleme İşlemler: Java 8 ile gelen, callback tabanlı asenkron işlem yönetimi.

Fonksiyonel Programlama Desteği: thenApply(), thenAccept(), exceptionally() gibi metodlarla esnek yapı.

Why? (Neden Kullanılır?)
Reaktif Programlama: Birden fazla asenkron işlemi zincirleme veya birleştirme.

Non-Blocking: Ana thread'i bloklamadan sonuç işleme.

How? (Nasıl Kullanılır?)
java
Copy
CompletableFuture.supplyAsync(() -> "Merhaba")
.thenApplyAsync(s -> s + " Dünya")
.thenAcceptAsync(System.out::println) // "Merhaba Dünya"
.exceptionally(ex -> {
System.out.println("Hata: " + ex.getMessage());
return null;
});
Avantajlar:

Non-Blocking: get() kullanmadan sonuç işlenebilir.

Zincirleme İşlemler: Birden fazla asenkron işlem birleştirilebilir.

Karşılaştırma Tablosu
Özellik	Thread	ExecutorService	Future	CompletableFuture
Kontrol Seviyesi	Manuel	Havuz Yönetimi	Sonuç Bekleme	Non-Blocking Zincirleme
Performans	Düşük (Yüksek Maliyet)	Yüksek (Optimize Havuz)	Orta (Bloklayıcı)	Yüksek (Non-Blocking)
Kullanım Kolaylığı	Düşük	Orta	Orta	Yüksek
Senaryo	Basit İşler	Çoklu İş Yönetimi	Sonuç Bekleme Gerektiren	Kompleks Asenkron Zincirler
Hangi Senaryoda Ne Kullanılmalı?
Thread:

Basit, tek seferlik işler (örneğin, arka planda bir log yazma).

ExecutorService:

Çoklu iş yükleri (örneğin, 100 REST API çağrısı).

Future:

Sonucu beklenen tekli asenkron işlemler (örneğin, dosya indirme).

CompletableFuture:

Non-blocking, zincirleme işlemler (örneğin, API A → API B → Veritabanı Kaydet).

Özet:

Thread: Temel, manuel yönetim.

ExecutorService: Optimize thread havuzu.

Future: Bloklayıcı sonuç bekleyici.

CompletableFuture: Non-blocking, reaktif ve zincirleme işlemler.

🚀 Tavsiye: Modern uygulamalarda CompletableFuture veya Reactive Streams kullanın!

### 


