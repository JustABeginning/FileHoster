# FileHoster

A Simple Spring Boot Application to Host Files

# Usage

- With CORS (Cross-Origin Resource Sharing)

  ```console

  foo@bar:~$ java -jar <BUILT_JAR>.jar <CORS_ORIGIN(s)...(space separated)> <API_ENDPOINT> <API_KEY> <TIME (in min)>

  ```

- With only `API_ENDPOINT`

  ```console

  foo@bar:~$ java -jar <BUILT_JAR>.jar <API_ENDPOINT> <API_KEY> <TIME (in min)>

  ```

- **Minimal Requirement:** With only `API_KEY`

  ```console

  foo@bar:~$ java -jar <BUILT_JAR>.jar <API_KEY> <TIME (in min)>

  ```

- **Note:** Path to `<BUILT_JAR>.jar` is `build/libs/<FILE_HOSTER>-SNAPSHOT.jar`

# Features

- Variable `CORS_ORIGIN(s)`, `API_ENDPOINT` (default `/api`) and, `API_KEY` (rolling with `TIME (in min)`, output path: `/storage/keys/keyFile.log`)

- Strict and concise `Content-Security-Policy` with implementation of `nonce` for both `<script>` and, `<style>` tags

- Selective access to API (Application Programming Interface) endpoints with Spring Security

- Form rendering and, validation with Thymeleaf

# Description

- Host files securely from your local machine and, make them shareable within the same subnet or, across it via port-forwarding

- By default, the application is hosted at `http://127.0.0.1:80/`

- Form to submit file(s) is available at the root mapping `/`, access to the same is made selective for better privacy

- Retrieve a saved file via `GET /<FILE_NAME>`

- API endpoints to manage saved file(s) are available at the following mappings:
  - `POST /<API_ENDPOINT>/getAllFiles`

    ```console

    {
      "apiKey": "<API_KEY>"
    }

    ```

  - `POST /<API_ENDPOINT>/renameFile`

    ```console

    {
      "apiKey": "<API_KEY>",
      "fileName": "<FILE_NAME>",
      "newFileName": "<NEW_FILE_NAME>"
    }

    ```

  - `POST /<API_ENDPOINT>/deleteFile`

    ```console

    {
      "apiKey": "<API_KEY>",
      "fileName": "<FILE_NAME>"
    }

    ```

  - `POST /<API_ENDPOINT>/deleteAllFiles`

    ```console

    {
      "apiKey": "<API_KEY>"
    }

    ```

  - **Note:** Access to these endpoints have been made selective for better privacy

# References

- [Getting Started | Handling Form Submission - Spring](https://spring.io/guides/gs/handling-form-submission)

- [Getting Started | Uploading Files - Spring](https://spring.io/guides/gs/uploading-files)

- [Spring Boot Thymeleaf Form Data Validation with Bean Validator](https://stackabuse.com/spring-boot-thymeleaf-form-data-validation-with-bean-validator/)

- [Integrate SQLite with Spring Boot](https://www.blackslate.io/articles/integrate-sqlite-with-spring-boot)

- [Content-Security-Policy Nonce with Spring Security](https://techblog.bozho.net/content-security-policy-nonce-with-spring-security/)

- [Spring MVC - allowing requests from localhost only to specific controller](https://stackoverflow.com/questions/23238876/spring-mvc-allowing-requests-from-localhost-only-to-specific-controller)
