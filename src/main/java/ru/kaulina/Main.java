package ru.kaulina;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;


public class Main {
    public static final String URL_PICTURE =
            "https://api.nasa.gov/planetary/apod?api_key=mWX1EwU8ZGNU8yTGqJNhKYJjeg6exgigDc11qTEO";

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(URL_PICTURE);
        CloseableHttpResponse response = client.execute(request);


        NasaPicturu nasaObject = mapper.readValue(response.getEntity().getContent(), NasaPicturu.class); //Преобразуем ответ в Java-объект NasaObject


        response = client.execute(new HttpGet(nasaObject.getUrl())); // Отправляем запрос и получаем ответ с нашей картинкой, скачать картинку

        HttpEntity entity = response.getEntity();


        String[] arr = nasaObject.getUrl().split("/");
        String fileName = arr[arr.length - 1];
        System.out.println("Имя файла: " + fileName);//Формируем автоматически название для файла


        FileOutputStream fos = new FileOutputStream(fileName);
        entity.writeTo(fos); //сохраняем в файл

        fos.close();
        client.close();
    }
}