package com.gundam.gatewaydemo;

import io.fria.lilo.GraphQLRequest;
import io.fria.lilo.IntrospectionFetchingMode;
import io.fria.lilo.Lilo;
import io.fria.lilo.RemoteSchemaSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LiloController {

  private static final String SERVER1_NAME = "SERVER1";
  private static final String SERVER1_BASE_URL = "http://localhost:8081";
  private static final String SERVER2_NAME = "SERVER2";
  private static final String SERVER2_BASE_URL = "http://localhost:8082";

  private final Lilo lilo;

  public LiloController() {

    this.lilo =
        Lilo.builder()
            .addSource(
                RemoteSchemaSource.create(
                    SERVER1_NAME,
                    new IntrospectionRetrieverImpl(SERVER1_BASE_URL),
                    new QueryRetrieverImpl(SERVER1_BASE_URL)))
            .addSource(
                RemoteSchemaSource.create(
                    SERVER2_NAME,
                    new IntrospectionRetrieverImpl(SERVER2_BASE_URL),
                    new QueryRetrieverImpl(SERVER2_BASE_URL)))
                .introspectionFetchingMode(IntrospectionFetchingMode.CACHE_UNTIL_INVALIDATION)
            .build();
  }

  @ResponseBody
  @PostMapping("/graphql")
  public @NonNull Map<String, Object> stitch(@RequestBody final @NonNull GraphQLRequest request) {
    System.out.println("srikanth");
    return this.lilo.stitch(request.toExecutionInput()).toSpecification();
  }
}