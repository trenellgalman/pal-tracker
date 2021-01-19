package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EnvController {

  private final Map<String, String> environment;

  public EnvController(
      @Value("${PORT:0}") String port,
      @Value("${MEMORY_LIMIT:0}") String memoryLimit,
      @Value("${CF_INSTANCE_INDEX:0}") String instanceIndex,
      @Value("${CF_INSTANCE_ADDR:Not Set}") String instanceAddress) {
    environment =
        Map.of(
            "PORT",
            port,
            "MEMORY_LIMIT",
            memoryLimit,
            "CF_INSTANCE_INDEX",
            instanceIndex,
            "CF_INSTANCE_ADDR",
            instanceAddress);
  }

  @GetMapping("/env")
  public Map<String, String> getEnv() {
    return environment;
  }
}
