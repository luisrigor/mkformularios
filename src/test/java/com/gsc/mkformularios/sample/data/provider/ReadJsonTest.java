package com.gsc.mkformularios.sample.data.provider;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


@NoArgsConstructor
public class ReadJsonTest {

    public <T> List<T> readJson(String filePath, Type tClass) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, tClass);
        }
    }

}
