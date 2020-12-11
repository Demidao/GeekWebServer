package ru.demid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {

    public static void main(String[] args) {
        //создание веб-серверного сокета
        try (ServerSocket serverSocket = new ServerSocket(8080)) { // по сути мы готовы принимать что-то от клиентов для дальнейшей работы

            System.out.println("Sever started");
            //Ждем соедиениния от клиента, запускаем в бесконечный цикл, чтобы ожидание не останавливалось. Далее читаем запросы
            //в отдельных потоках
            while (true) {
                Socket socket = serverSocket.accept(); //сервер получает запрос на подключение от клиентского сокета. Просто ждем
                System.out.println("New client connected...");
                //запускаем поток для обработки запросов и формирования ответов без остановки работы сервера

                new Thread(() -> {
                    try {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                             PrintWriter writer = new PrintWriter(socket.getOutputStream());) {

                            while (!reader.ready());
                                System.out.println("+");

                                while (reader.ready()) {
                                    System.out.println(reader.readLine());
                                    //получим по факту запрос GET, если запросим на localhost:8080
                    /*
                    GET / HTTP/1.1
                    Host: localhost:8080
                    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0
                    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,;q=0.8
                    Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3
                    Accept-Encoding: gzip, deflate
                    Connection: keep-alive
                    Upgrade-Insecure-Requests: 1
                    */
                                }

                            //теперь нужно на него ответить что-то, для этого создаем поток записи

                            writer.println("HTTP/1.1 200 OK");//указываем в ответе версию протокола http, код ответа 200 и словесное обозначение.
                            writer.println("Content-type: text/html; charset=utf-8");//Отправляем текстовый документ в формате html с соответствующей кодировкой.
                            writer.println();// после заголовков обязательно нужно оставить 1 пустую строку, далее передаем наш докумень построчно.
                            writer.println("<h1>Это заголовок</h1>");
                            writer.println("<h2>Это заголовок</h2>");
                            writer.println("<h3>Это заголовок</h3>");

                            writer.flush();
                            System.out.println("Client disconnected...");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                //теперь можно читать инфу, полученную от клиента. Читаем поток ввода/чтения...

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
