package com.corder.reactiveprogrammingdemoapp;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReactiveTutorial {
    private Mono<String> testMono() {
        return Mono.justOrEmpty("Java")
                .log();
    }

    private Flux<String> testFlux() {
        return Flux.just("Java", "Cpp", "AWS")
                .log();
    }

    //test iterable
    private Flux<String> testFlux2() {
        List<String> list = List.of("Java", "Cpp", "AWS");
        return Flux.fromIterable(list);
    }

    //test map
    private Flux<String> testFlux3() {
        List<String> list = List.of("Java", "Cpp", "AWS");
        Flux<String> flux = Flux.fromIterable(list);
        return flux.map(s -> s.toUpperCase(Locale.ROOT));
    }

    //test flatMap
    private Flux<String> testFlux4() {
        List<String> list = List.of("Java", "Cpp", "AWS");
        Flux<String> flux = Flux.fromIterable(list);
        return flux.flatMap(s -> Mono.just(s.toUpperCase(Locale.ROOT)));
    }

    //test skip
    private Flux<String> testFlux5() {
        List<String> list = List.of("Java", "Cpp", "AWS");
        Flux<String> flux = Flux.fromIterable(list);
        return flux.skip(2);
    }

    //test delayElements
    private Flux<String> testFlux6() {
        List<String> list = List.of("Java", "Cpp", "AWS");
        Flux<String> flux = Flux.fromIterable(list);
        return flux.delayElements(Duration.ofSeconds(1));
    }

    //test delayElements,skip
    private Flux<String> testFlux7() {
        Flux<String> flux = Flux.just("Java", "Cpp", "AWS", "Kubernetes", "Rust");
        flux.delayElements(Duration.ofSeconds(1));
        return flux.skip(Duration.ofMillis(2010));
    }

    //test testComplexSkip
    private Flux<Integer> testComplexSkip() {
        Flux<Integer> flux = Flux.range(1, 20);
        return flux.skipWhile(data -> data < 10);
    }

    //test testConcat
    private Flux<Integer> testConcat() {
        Flux<Integer> flux1 = Flux.range(1, 20);
        Flux<Integer> flux2 = Flux.range(101, 20);
        return Flux.concat(flux1, flux2);
    }

    //test testmerge
    private Flux<Integer> testMerge() {
        Flux<Integer> flux1 = Flux.range(1, 20);
        Flux<Integer> flux2 = Flux.range(101, 20);
        return Flux.merge(flux1, flux2);
    }

    //test zip
    private Flux<Tuple2<Integer, Integer>> testZip() {
        Flux<Integer> flux1 = Flux.range(1, 20);
        Flux<Integer> flux2 = Flux.range(101, 20);
        return Flux.zip(flux1, flux2);
    }

    //testCollect
    private Mono<List<Integer>> testCollect() {
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.collectList();
    }

    //testBuffer
    private Flux<List<Integer>> testBuffer() {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1000));
        return flux.buffer(Duration.ofMillis(3_100));
    }

    private Mono<Map<Integer, Integer>> testMapCollection() {
        //a, a*a
        //5, 25
        //6, 36
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.collectMap(integer -> integer, integer -> integer * integer);
    }

    private Flux<Integer> testDoFunctions() {
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.doOnEach(signal -> {
            if (signal.getType() == SignalType.ON_COMPLETE) {
                System.out.println("I am done!");
            } else {
                System.out.println(signal.get());
            }
        });
    }

    //doOnComplete
    private Flux<Integer> testDoFunctionsDoOnComplete() {
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.doOnComplete(() -> System.out.println("I am complete"));
    }

    private Flux<Integer> testDoFunctionsDoOnNext() {
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.doOnNext(data -> System.out.println(data));
    }

    private Flux<Integer> testDoFunctionsDoSubscribe() {
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.doOnSubscribe(susbscription -> System.out.println("Subscribed"));
    }

    private Flux<Integer> testDoOnCancel() {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));
        return flux.doOnCancel(() -> System.out.println("Cancelled!"));
    }

    private Flux<Integer> testErrorHandlingOnErrorContinue() {
        Flux<Integer> flux = Flux.range(1, 10)
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected number!");
                    }
                    return integer;
                });
        return flux
                .onErrorContinue((throwable, o) -> System.out.println("Don't worry about " + o));
    }

    private Flux<Integer> testErrorHandlingOnErrorReturn() {
        Flux<Integer> flux = Flux.range(1, 10)
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected number!");
                    }
                    return integer;
                });
        return flux
                .onErrorReturn(RuntimeException.class, -1)
                .onErrorReturn(ArithmeticException.class, -2);
    }

    private Flux<Integer> testErrorHandlingOnErrorResume() {
        Flux<Integer> flux = Flux.range(1, 10)
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected number!");
                    }
                    return integer;
                });
        return flux
                .onErrorResume(throwable -> Flux.range(100, 5));
    }

    private Flux<Integer> testErrorHandlingOnErrorMap() {
        Flux<Integer> flux = Flux.range(1, 10)
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected number!");
                    }
                    return integer;
                });
        return flux
                .onErrorMap(throwable -> new UnsupportedOperationException(throwable.getMessage()));
    }




    static void main(String[] args) throws InterruptedException {
        ReactiveTutorial reactiveTutorial = new ReactiveTutorial();
        //test Mono
        // reactiveTutorial.testMono()
        // .subscribe(data -> System.out.println(data));

        //test Flux
        //reactiveTutorial.testFlux().subscribe(data -> System.out.println(data));

        //test itterable
        // reactiveTutorial.testFlux2().subscribe(data -> System.out.println(data));
        //test map
        //reactiveTutorial.testFlux3().subscribe(data -> System.out.println(data));

        //test flatMap
        //reactiveTutorial.testFlux4().subscribe(System.out::println);

        //test skip
        // reactiveTutorial.testFlux5().subscribe(data -> System.out.println(data));
        //reactiveTutorial.testComplexSkip().subscribe(System.out::println);


//         try {
//             //test delayElement
//             reactiveTutorial.testFlux6().subscribe(data -> System.out.println(data));
//             Thread.sleep(10000);
//         } catch (InterruptedException e) {
//             throw new RuntimeException(e);
//         }


        //test delayElement,Skip
        //reactiveTutorial.testFlux7().subscribe(data -> System.out.println(data));
        // Thread.sleep(10000);

        //test concat
        // reactiveTutorial.testConcat().subscribe(data -> System.out.println(data));

        //testMerge
        // reactiveTutorial.testMerge().subscribe(data -> System.out.println(data));

        //testZip
        //reactiveTutorial.testZip().subscribe(data -> System.out.println(data));

        //testCollect
        //reactiveTutorial.testCollect().subscribe(data -> System.out.println(data));
        //List<Integer> outout = reactiveTutorial.testCollect().block();
        //System.out.println(outout);

        //testBuffer
        // reactiveTutorial.testBuffer().subscribe(data -> System.out.println(data));
        //  Thread.sleep(10000);

        //testMapCollection
        // reactiveTutorial.testMapCollection().subscribe(data -> System.out.println(data));

        //testDoFunctions
        // reactiveTutorial.testDoFunctions().subscribe(data -> System.out.println(data));
        // reactiveTutorial.testDoFunctionsDoOnComplete().subscribe(data -> System.out.println(data));

        //testDoFunctionsDoOnNext
        // reactiveTutorial.testDoFunctionsDoOnNext().subscribe();

        //testDoFunctionsDoSubscribe
        //reactiveTutorial.testDoFunctionsDoSubscribe().subscribe(data -> System.out.println(data));
        // reactiveTutorial.testDoFunctionsDoSubscribe().subscribe(data -> System.out.println(data));

        //testDoOnCancel
        //Disposable disposable = reactiveTutorial.testDoOnCancel().subscribe(data -> System.out.println(data));
        //Thread.sleep(3500);
        // disposable.dispose();

        //testErrorHandlingOnErrorContinue
        //reactiveTutorial.testErrorHandlingOnErrorContinue().subscribe(data -> System.out.println(data));

        //testErrorHandlingOnErrorReturn
        //reactiveTutorial.testErrorHandlingOnErrorReturn().subscribe(data -> System.out.println(data));

        //testErrorHandlingOnErrorResume
        //reactiveTutorial.testErrorHandlingOnErrorResume().subscribe(data -> System.out.println(data));

        //testErrorHandlingOnErrorMap
        reactiveTutorial.testErrorHandlingOnErrorMap().subscribe(data -> System.out.println(data));


    }

}

