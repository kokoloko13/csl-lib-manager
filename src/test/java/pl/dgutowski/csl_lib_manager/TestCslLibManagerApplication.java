package pl.dgutowski.csl_lib_manager;

import org.springframework.boot.SpringApplication;

public class TestCslLibManagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(CslLibManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
