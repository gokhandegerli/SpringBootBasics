package com.degerli.SpringBootBasics.domain.order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyVsParallelismDemo {

  private static final int TASK_COUNT = 10;
  private static final int LONG_TASK_DURATION = 2000; // 2 saniye
  private static final int SHORT_TASK_DURATION = 100; // 100 milisaniye

  public static void main(String[] args) {
    try {
      System.out.println("Uygulama başlatıldı");
      System.out.println();

      // Senkron işlem
      runSynchronousProcess();
      System.out.println("\n");

      // Eşzamanlı işlem (Concurrency)
      runConcurrentProcess();
      System.out.println("\n");

      // Asenkron işlem (CompletableFuture)
      runAsyncProcess();
      System.out.println("\n");

      // Paralel işlem (ParallelStream)
      runParallelStreamProcess();
      System.out.println("\n");

      System.out.println("Tüm işlemler tamamlandı");
    } catch (Exception e) {
      System.out.println("Ana işlemde hata: " + e.getMessage());
    }
  }

  /**
   * Senkron işlem - Tüm işlemler sırayla yapılır
   */
  private static void runSynchronousProcess() {
    System.out.println("=== SENKRON İŞLEM BAŞLADI ===");
    long startTime = System.currentTimeMillis();

    try {
      // İlk uzun süren işlem
      processLongRunningTask("İlk uzun işlem", startTime);

      // Döngüsel işlemler
      for (int i = 1; i <= TASK_COUNT; i++) {
        processTask(i, startTime);
      }

      // İkinci uzun süren işlem
      processLongRunningTask("İkinci uzun işlem", startTime);

      long endTime = System.currentTimeMillis();
      System.out.println(
          "Senkron işlem tamamlandı - Toplam süre: " + (endTime - startTime) + " ms");
    } catch (Exception e) {
      System.out.println("Senkron işlemde hata: " + e.getMessage());
    }
  }

  /**
   * Eşzamanlı işlem - Uzun süren işlemler arka planda çalışır
   */
  private static void runConcurrentProcess() {
    System.out.println("=== EŞZAMANLI İŞLEM (CONCURRENCY) BAŞLADI ===");
    long startTime = System.currentTimeMillis();

    try {
      ExecutorService executor = Executors.newFixedThreadPool(2); // Sadece 2 thread

      // İlk uzun süren işlemi arka planda başlat
      CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
        try {
          processLongRunningTask("İlk uzun işlem (arka planda)", startTime);
        } catch (Exception e) {
          System.out.println("Arka plan işleminde hata: " + e.getMessage());
        }
      }, executor);

      // Döngüsel işlemler (ana thread'de sırayla)
      for (int i = 1; i <= TASK_COUNT; i++) {
        processTask(i, startTime);
      }

      // İkinci uzun süren işlemi arka planda başlat
      CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
        try {
          processLongRunningTask("İkinci uzun işlem (arka planda)", startTime);
        } catch (Exception e) {
          System.out.println("Arka plan işleminde hata: " + e.getMessage());
        }
      }, executor);

      System.out.println(
          "Ana iş akışı tamamlandı - Geçen süre: " + (System.currentTimeMillis() - startTime)
              + " ms");

      // Uzun işlemlerin tamamlanmasını bekle
      CompletableFuture.allOf(future1, future2).join();
      executor.shutdown();

      long endTime = System.currentTimeMillis();
      System.out.println(
          "Eşzamanlı işlem tamamlandı - Toplam süre: " + (endTime - startTime) + " ms");
    } catch (Exception e) {
      System.out.println("Eşzamanlı işlemde hata: " + e.getMessage());
    }
  }

  /**
   * Asenkron işlem - Tüm işlemler asenkron olarak çalışır
   */
  private static void runAsyncProcess() {
    System.out.println("=== ASENKRON İŞLEM (COMPLETABLEFUTURE) BAŞLADI ===");
    long startTime = System.currentTimeMillis();

    try {
      ExecutorService executor = Executors.newFixedThreadPool(
          TASK_COUNT + 2); // Tüm işlemler için yeterli thread
      List<CompletableFuture<Void>> futures = new ArrayList<>();

      // İlk uzun süren işlemi asenkron başlat
      futures.add(CompletableFuture.runAsync(() -> {
        try {
          processLongRunningTask("İlk uzun işlem (asenkron)", startTime);
        } catch (Exception e) {
          System.out.println("Asenkron işlemde hata: " + e.getMessage());
        }
      }, executor));

      // Döngüsel işlemleri asenkron başlat
      for (int i = 1; i <= TASK_COUNT; i++) {
        final int taskId = i;
        futures.add(CompletableFuture.runAsync(() -> {
          try {
            processTask(taskId, startTime);
          } catch (Exception e) {
            System.out.println("Asenkron işlemde hata (" + taskId + "): " + e.getMessage());
          }
        }, executor));
      }

      // İkinci uzun süren işlemi asenkron başlat
      futures.add(CompletableFuture.runAsync(() -> {
        try {
          processLongRunningTask("İkinci uzun işlem (asenkron)", startTime);
        } catch (Exception e) {
          System.out.println("Asenkron işlemde hata: " + e.getMessage());
        }
      }, executor));

      // Tüm işlemlerin tamamlanmasını bekle
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
      executor.shutdown();

      long endTime = System.currentTimeMillis();
      System.out.println(
          "Asenkron işlem tamamlandı - Toplam süre: " + (endTime - startTime) + " ms");
    } catch (Exception e) {
      System.out.println("Asenkron işlemde hata: " + e.getMessage());
    }
  }

  /**
   * Paralel işlem - ParallelStream kullanarak
   */
  private static void runParallelStreamProcess() {
    System.out.println("=== PARALEL İŞLEM (PARALLELSTREAM) BAŞLADI ===");
    long startTime = System.currentTimeMillis();

    try {
      // Tüm işlemleri bir listeye ekle (uzun işlemler + normal işlemler)
      List<Runnable> allTasks = new ArrayList<>();

      // İlk uzun işlem
      allTasks.add(() -> {
        try {
          processLongRunningTask("İlk uzun işlem (parallelStream)", startTime);
        } catch (Exception e) {
          System.out.println("ParallelStream işleminde hata: " + e.getMessage());
        }
      });

      // Döngüsel işlemler
      for (int i = 1; i <= TASK_COUNT; i++) {
        final int taskId = i;
        allTasks.add(() -> {
          try {
            processTask(taskId, startTime);
          } catch (Exception e) {
            System.out.println(
                "ParallelStream işleminde hata (" + taskId + "): " + e.getMessage());
          }
        });
      }

      // İkinci uzun işlem
      allTasks.add(() -> {
        try {
          processLongRunningTask("İkinci uzun işlem (parallelStream)", startTime);
        } catch (Exception e) {
          System.out.println("ParallelStream işleminde hata: " + e.getMessage());
        }
      });

      // Tüm işlemleri paralel olarak çalıştır
      allTasks.parallelStream().forEach(Runnable::run);

      long endTime = System.currentTimeMillis();
      System.out.println(
          "Paralel işlem (ParallelStream) tamamlandı - Toplam süre: " + (endTime - startTime)
              + " ms");
    } catch (Exception e) {
      System.out.println("ParallelStream işleminde hata: " + e.getMessage());
    }
  }

  /**
   * Tekil bir işlemi gerçekleştirir
   */
  private static void processTask(int taskId, long startTime) throws Exception {
    System.out.println(
        "İşlem " + taskId + " başladı - Thread: " + Thread.currentThread().getName()
            + " - Geçen süre: " + (System.currentTimeMillis() - startTime) + " ms");

    Thread.sleep(SHORT_TASK_DURATION);

    System.out.println(
        "İşlem " + taskId + " tamamlandı - Thread: " + Thread.currentThread().getName()
            + " - Geçen süre: " + (System.currentTimeMillis() - startTime) + " ms");
  }

  /**
   * Uzun süren bir işlemi gerçekleştirir
   */
  private static void processLongRunningTask(String taskName, long startTime)
      throws Exception {
    System.out.println(
        taskName + " başladı - Thread: " + Thread.currentThread().getName() + " - Geçen süre: "
            + (System.currentTimeMillis() - startTime) + " ms");

    Thread.sleep(LONG_TASK_DURATION);

    System.out.println(taskName + " tamamlandı - Thread: " + Thread.currentThread().getName()
        + " - Geçen süre: " + (System.currentTimeMillis() - startTime) + " ms");
  }
}


/*
Uygulama başlatıldı

=== SENKRON İŞLEM BAŞLADI ===
İlk uzun işlem başladı - Thread: main - Geçen süre: 0 ms
İlk uzun işlem tamamlandı - Thread: main - Geçen süre: 2005 ms
İşlem 1 başladı - Thread: main - Geçen süre: 2006 ms
İşlem 1 tamamlandı - Thread: main - Geçen süre: 2117 ms
İşlem 2 başladı - Thread: main - Geçen süre: 2119 ms
İşlem 2 tamamlandı - Thread: main - Geçen süre: 2223 ms
İşlem 3 başladı - Thread: main - Geçen süre: 2229 ms
İşlem 3 tamamlandı - Thread: main - Geçen süre: 2335 ms
İşlem 4 başladı - Thread: main - Geçen süre: 2335 ms
İşlem 4 tamamlandı - Thread: main - Geçen süre: 2439 ms
İşlem 5 başladı - Thread: main - Geçen süre: 2439 ms
İşlem 5 tamamlandı - Thread: main - Geçen süre: 2541 ms
İşlem 6 başladı - Thread: main - Geçen süre: 2541 ms
İşlem 6 tamamlandı - Thread: main - Geçen süre: 2646 ms
İşlem 7 başladı - Thread: main - Geçen süre: 2646 ms
İşlem 7 tamamlandı - Thread: main - Geçen süre: 2747 ms
İşlem 8 başladı - Thread: main - Geçen süre: 2747 ms
İşlem 8 tamamlandı - Thread: main - Geçen süre: 2851 ms
İşlem 9 başladı - Thread: main - Geçen süre: 2851 ms
İşlem 9 tamamlandı - Thread: main - Geçen süre: 2956 ms
İşlem 10 başladı - Thread: main - Geçen süre: 2957 ms
İşlem 10 tamamlandı - Thread: main - Geçen süre: 3062 ms
İkinci uzun işlem başladı - Thread: main - Geçen süre: 3062 ms
İkinci uzun işlem tamamlandı - Thread: main - Geçen süre: 5066 ms
Senkron işlem tamamlandı - Toplam süre: 5067 ms


=== EŞZAMANLI İŞLEM (CONCURRENCY) BAŞLADI ===
İşlem 1 başladı - Thread: main - Geçen süre: 9 ms
İlk uzun işlem (arka planda) başladı - Thread: pool-1-thread-1 - Geçen süre: 9 ms
İşlem 1 tamamlandı - Thread: main - Geçen süre: 115 ms
İşlem 2 başladı - Thread: main - Geçen süre: 115 ms
İşlem 2 tamamlandı - Thread: main - Geçen süre: 218 ms
İşlem 3 başladı - Thread: main - Geçen süre: 219 ms
İşlem 3 tamamlandı - Thread: main - Geçen süre: 323 ms
İşlem 4 başladı - Thread: main - Geçen süre: 323 ms
İşlem 4 tamamlandı - Thread: main - Geçen süre: 427 ms
İşlem 5 başladı - Thread: main - Geçen süre: 428 ms
İşlem 5 tamamlandı - Thread: main - Geçen süre: 529 ms
İşlem 6 başladı - Thread: main - Geçen süre: 530 ms
İşlem 6 tamamlandı - Thread: main - Geçen süre: 632 ms
İşlem 7 başladı - Thread: main - Geçen süre: 632 ms
İşlem 7 tamamlandı - Thread: main - Geçen süre: 735 ms
İşlem 8 başladı - Thread: main - Geçen süre: 735 ms
İşlem 8 tamamlandı - Thread: main - Geçen süre: 841 ms
İşlem 9 başladı - Thread: main - Geçen süre: 841 ms
İşlem 9 tamamlandı - Thread: main - Geçen süre: 945 ms
İşlem 10 başladı - Thread: main - Geçen süre: 946 ms
İşlem 10 tamamlandı - Thread: main - Geçen süre: 1050 ms
İkinci uzun işlem (arka planda) başladı - Thread: pool-1-thread-2 - Geçen süre: 1054 ms
Ana iş akışı tamamlandı - Geçen süre: 1054 ms
İlk uzun işlem (arka planda) tamamlandı - Thread: pool-1-thread-1 - Geçen süre: 2011 ms
İkinci uzun işlem (arka planda) tamamlandı - Thread: pool-1-thread-2 - Geçen süre: 3059 ms
Eşzamanlı işlem tamamlandı - Toplam süre: 3061 ms


=== ASENKRON İŞLEM (COMPLETABLEFUTURE) BAŞLADI ===
İlk uzun işlem (asenkron) başladı - Thread: pool-2-thread-1 - Geçen süre: 2 ms
İşlem 1 başladı - Thread: pool-2-thread-2 - Geçen süre: 4 ms
İşlem 2 başladı - Thread: pool-2-thread-3 - Geçen süre: 4 ms
İşlem 3 başladı - Thread: pool-2-thread-4 - Geçen süre: 4 ms
İşlem 4 başladı - Thread: pool-2-thread-5 - Geçen süre: 4 ms
İşlem 5 başladı - Thread: pool-2-thread-6 - Geçen süre: 4 ms
İşlem 6 başladı - Thread: pool-2-thread-7 - Geçen süre: 5 ms
İşlem 7 başladı - Thread: pool-2-thread-8 - Geçen süre: 5 ms
İşlem 8 başladı - Thread: pool-2-thread-9 - Geçen süre: 5 ms
İşlem 9 başladı - Thread: pool-2-thread-10 - Geçen süre: 5 ms
İşlem 10 başladı - Thread: pool-2-thread-11 - Geçen süre: 5 ms
İkinci uzun işlem (asenkron) başladı - Thread: pool-2-thread-12 - Geçen süre: 6 ms
İşlem 1 tamamlandı - Thread: pool-2-thread-2 - Geçen süre: 106 ms
İşlem 7 tamamlandı - Thread: pool-2-thread-8 - Geçen süre: 106 ms
İşlem 2 tamamlandı - Thread: pool-2-thread-3 - Geçen süre: 109 ms
İşlem 6 tamamlandı - Thread: pool-2-thread-7 - Geçen süre: 109 ms
İşlem 10 tamamlandı - Thread: pool-2-thread-11 - Geçen süre: 109 ms
İşlem 9 tamamlandı - Thread: pool-2-thread-10 - Geçen süre: 109 ms
İşlem 3 tamamlandı - Thread: pool-2-thread-4 - Geçen süre: 109 ms
İşlem 5 tamamlandı - Thread: pool-2-thread-6 - Geçen süre: 109 ms
İşlem 4 tamamlandı - Thread: pool-2-thread-5 - Geçen süre: 109 ms
İşlem 8 tamamlandı - Thread: pool-2-thread-9 - Geçen süre: 110 ms
İlk uzun işlem (asenkron) tamamlandı - Thread: pool-2-thread-1 - Geçen süre: 2007 ms
İkinci uzun işlem (asenkron) tamamlandı - Thread: pool-2-thread-12 - Geçen süre: 2011 ms
Asenkron işlem tamamlandı - Toplam süre: 2012 ms


=== PARALEL İŞLEM (PARALLELSTREAM) BAŞLADI ===
İşlem 7 başladı - Thread: main - Geçen süre: 6 ms
İşlem 3 başladı - Thread: ForkJoinPool.commonPool-worker-1 - Geçen süre: 6 ms
İşlem 1 başladı - Thread: ForkJoinPool.commonPool-worker-3 - Geçen süre: 7 ms
İşlem 5 başladı - Thread: ForkJoinPool.commonPool-worker-5 - Geçen süre: 7 ms
İlk uzun işlem (parallelStream) başladı - Thread: ForkJoinPool.commonPool-worker-7 - Geçen süre: 7 ms
İşlem 10 başladı - Thread: ForkJoinPool.commonPool-worker-2 - Geçen süre: 7 ms
İşlem 4 başladı - Thread: ForkJoinPool.commonPool-worker-6 - Geçen süre: 7 ms
İşlem 6 başladı - Thread: ForkJoinPool.commonPool-worker-4 - Geçen süre: 7 ms
İşlem 7 tamamlandı - Thread: main - Geçen süre: 110 ms
İşlem 8 başladı - Thread: main - Geçen süre: 110 ms
İşlem 3 tamamlandı - Thread: ForkJoinPool.commonPool-worker-1 - Geçen süre: 111 ms
İşlem 9 başladı - Thread: ForkJoinPool.commonPool-worker-1 - Geçen süre: 112 ms
İşlem 1 tamamlandı - Thread: ForkJoinPool.commonPool-worker-3 - Geçen süre: 112 ms
İşlem 2 başladı - Thread: ForkJoinPool.commonPool-worker-3 - Geçen süre: 112 ms
İşlem 5 tamamlandı - Thread: ForkJoinPool.commonPool-worker-5 - Geçen süre: 113 ms
İşlem 4 tamamlandı - Thread: ForkJoinPool.commonPool-worker-6 - Geçen süre: 113 ms
İşlem 6 tamamlandı - Thread: ForkJoinPool.commonPool-worker-4 - Geçen süre: 113 ms
İşlem 10 tamamlandı - Thread: ForkJoinPool.commonPool-worker-2 - Geçen süre: 113 ms
İkinci uzun işlem (parallelStream) başladı - Thread: ForkJoinPool.commonPool-worker-5 - Geçen süre: 113 ms
İşlem 8 tamamlandı - Thread: main - Geçen süre: 214 ms
İşlem 2 tamamlandı - Thread: ForkJoinPool.commonPool-worker-3 - Geçen süre: 214 ms
İşlem 9 tamamlandı - Thread: ForkJoinPool.commonPool-worker-1 - Geçen süre: 216 ms
İlk uzun işlem (parallelStream) tamamlandı - Thread: ForkJoinPool.commonPool-worker-7 - Geçen süre: 2010 ms
Disconnected from the target VM, address: 'localhost:51539', transport: 'socket'
İkinci uzun işlem (parallelStream) tamamlandı - Thread: ForkJoinPool.commonPool-worker-5 - Geçen süre: 2117 ms
Paralel işlem (ParallelStream) tamamlandı - Toplam süre: 2118 ms


Tüm işlemler tamamlandı*/


/*
yukaridaki ciktinin yorumu:

Senkron İşlem
Senkron işlemde tüm görevler sırayla, tek bir thread (main) üzerinde çalıştırılır. Çıktıda
görüldüğü gibi, önce ilk uzun işlem (2 saniye), ardından 10 kısa işlem (her biri 100ms) ve
son olarak ikinci uzun işlem (2 saniye) tamamlanmıştır. Toplam süre yaklaşık 5 saniye
olmuştur çünkü her işlem bir önceki tamamlanmadan başlayamaz. Bu yaklaşım, kaynakları
verimli kullanmaz ve toplam işlem süresi tüm görevlerin süresinin toplamına eşittir (2s +
10×0.1s + 2s ≈ 5s).

Eşzamanlı İşlem (Concurrency)
    Eşzamanlı işlemde, uzun süren görevler arka planda ayrı thread'lerde çalışırken, ana
    thread kısa işlemleri sırayla yürütür. Çıktıda görüldüğü gibi, ilk uzun işlem arka
    planda (pool-1-thread-1) başlatılmış, ana thread kısa işlemleri yapmış, sonra ikinci
    uzun işlem başka bir thread'de (pool-1-thread-2) başlatılmıştır. Ana iş akışı yaklaşık 1
     saniyede tamamlanmış, ancak uzun işlemlerin bitmesi beklendiği için toplam süre 3
     saniye olmuştur. Bu yaklaşım, ana iş akışını bloke etmeden uzun işlemleri arka planda
     yaparak yanıt verme süresini iyileştirir.

    Asenkron İşlem (CompletableFuture)
    Asenkron işlemde, tüm görevler için ayrı thread'ler oluşturularak hepsi aynı anda
    başlatılır. Çıktıda görüldüğü gibi, tüm işlemler neredeyse aynı anda başlamış
    (pool-2-thread-1 ila pool-2-thread-12), kısa işlemler yaklaşık 100ms'de tamamlanmış,
    uzun işlemler ise 2 saniye sürmüştür. Toplam süre, en uzun süren işlemin süresi kadardır
     (2 saniye). Bu yaklaşım, çok sayıda thread kullanarak maksimum paralellik sağlar ve CPU
      kaynaklarını tam kapasite kullanır, böylece toplam işlem süresi önemli ölçüde azalır.

Paralel İşlem (ParallelStream)
    ParallelStream yaklaşımında, Java'nın ForkJoinPool mekanizması kullanılarak işlemler
    paralel çalıştırılır. Çıktıda görüldüğü gibi, işlemler farklı thread'lerde (main ve
    ForkJoinPool.commonPool-worker-X) eşzamanlı olarak başlatılmış, kısa işlemler hızla
    tamamlanmış, uzun işlemler ise 2 saniye sürmüştür. Toplam süre yine en uzun süren
    işlemin süresi kadardır (2 saniye). Bu yaklaşım, thread havuzu yönetimini Java'ya
    bırakarak kod karmaşıklığını azaltır ve sistem kaynaklarını verimli kullanır, özellikle
    veri işleme ve dönüştürme işlemleri için idealdir.
*/
