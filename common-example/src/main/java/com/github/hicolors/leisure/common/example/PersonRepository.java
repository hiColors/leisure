package com.github.hicolors.leisure.common.example;

import com.github.hicolors.leisure.common.jpa.customiz.repository.ColorsRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ColorsRepository<Person, Long> {
}
