package com.github.hicolors.leisure.common.example.a;

import com.github.hicolors.leisure.common.example.a.bean.Person;
import com.github.hicolors.leisure.common.example.a.repository.PersonRepository;
import com.github.hicolors.leisure.common.model.expression.ColorsExpression;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @Test
    public void test() {
//        Optional<Person> p = repository.findById(2L);
//        p.get();

        List<ColorsExpression> list = new ArrayList<>();
//        list.add(new ColorsExpression("BETWEEN_id", "1~2"));
        list.add(new ColorsExpression("EQ_id", 1));
        List<Person> persons = repository.findAll(list);
        log.info("查询结果 ： {}", persons.size());
    }
}