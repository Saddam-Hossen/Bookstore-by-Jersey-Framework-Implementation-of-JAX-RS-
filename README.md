To run a **Jersey JAX-RS app on Tomcat**, follow this minimal setup using **Maven** and **Jersey Servlet container integration**.

### This project was runned in netbean



---

### ✅ **1. `pom.xml` Configuration**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>jersey-tomcat-demo</artifactId>
  <version>1.0</version>
  <packaging>war</packaging>

  
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Jakarta REST API (Provided by container like Tomcat) -->
       <!-- https://mvnrepository.com/artifact/jakarta.ws.rs/jakarta.ws.rs-api -->
    <dependency>
      <groupId>jakarta.ws.rs</groupId>
       <artifactId>jakarta.ws.rs-api</artifactId>
        <version>4.0.0</version>
    </dependency>

       <!-- https://mvnrepository.com/artifact/org.glassfish.jersey.containers/jersey-container-servlet -->
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-servlet</artifactId>
    <version>4.0.0-M2</version>
</dependency>

        <!-- https://mvnrepository.com/artifact/org.glassfish.jersey.core/jersey-server -->
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-server</artifactId>
    <version>4.0.0-M2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.inject/jersey-hk2 -->
<dependency>
    <groupId>org.glassfish.jersey.inject</groupId>
    <artifactId>jersey-hk2</artifactId>
    <version>4.0.0-M2</version>
</dependency>
<dependency>
  <groupId>com.sun.activation</groupId>
  <artifactId>jakarta.activation</artifactId>
  <version>2.0.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish.hk2</groupId>
    <artifactId>hk2-api</artifactId>
    <version>4.0.0-M2</version>
</dependency>

        <!-- JSON Binding for REST -->
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-json-binding</artifactId>
            <version>3.1.2</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>bookstore</finalName>
        <plugins>
            <!-- Maven Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                </configuration>
            </plugin>

            <!-- Maven WAR Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
            </plugin>
        </plugins>
    </build>
</project>

```

---

### ✅ **2. Create Jersey Resource**

**`src/main/java/com/example/HelloResource.java`:**

```java
package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
        return "Hello from Jersey on Tomcat!";
    }
}
```

---

### ✅ **3. Create Jersey Config Class**

**`src/main/java/com/example/ApplicationConfig.java`:**

```java
package com.example;

import org.glassfish.jersey.server.ResourceConfig;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages("com.example");
    }
}
```

---

### ✅ **4. `web.xml` (Optional)**

If you’re using Servlet 3.0+, this is optional. But for older Tomcat versions:

**`src/main/webapp/WEB-INF/web.xml`:**

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee" version="3.1">
    <display-name>Jersey Tomcat App</display-name>
</web-app>
```

---

### ✅ **5. Deploy to Tomcat**

1. Build the WAR:

   ```bash
   mvn clean package
   ```
2. Copy the generated `target/jersey-tomcat-demo.war` to Tomcat's `webapps/` folder.
3. Start Tomcat.
4. Visit:
   [http://localhost:8080/jersey-tomcat-demo/api/hello](http://localhost:8080/jersey-tomcat-demo/api/hello)

You should see:

```
Hello from Jersey on Tomcat!
```

---




#Here’s the **official JAX-RS documentation** and key resources:

---

### ✅ **Official Documentation**

* **Jakarta RESTful Web Services (JAX-RS) Specification:**
  📄 [https://jakarta.ee/specifications/restful-ws/](https://jakarta.ee/specifications/restful-ws/)

  This is the most current, authoritative source. Jakarta EE took over from Java EE (Oracle) and maintains JAX-RS under its umbrella.

---

### 📚 **Key Sections to Explore**

1. **Basic Concepts:**

   * Resource classes (`@Path`)
   * HTTP methods (`@GET`, `@POST`, `@PUT`, `@DELETE`)
   * Content negotiation (`@Produces`, `@Consumes`)

2. **Advanced Features:**

   * Exception handling (`ExceptionMapper`)
   * Filters and interceptors
   * Dependency injection
   * HATEOAS support

3. **Deploying JAX-RS:**

   * On Servlet containers (e.g., Tomcat)
   * With Jakarta EE full profile (e.g., Payara, WildFly)
   * With embedded servers (via Jersey/Grizzly)

---

### 🔗 Additional Helpful Resources

* **Jersey (JAX-RS reference implementation):**
  📄 [https://eclipse-ee4j.github.io/jersey/](https://eclipse-ee4j.github.io/jersey/)

* **Jakarta RESTful Web Services API Javadoc (v3.1):**
  📄 [https://jakarta.ee/specifications/restful-ws/3.1/apidocs/](https://jakarta.ee/specifications/restful-ws/3.1/apidocs/)

* **Tutorials (e.g., Baeldung):**
  📘 [https://www.baeldung.com/jax-rs](https://www.baeldung.com/jax-rs)

---



Here’s a direct comparison of **Jersey** vs other popular Java web frameworks for building RESTful APIs:

---

### 🔶 1. **Jersey** (Reference Implementation of JAX-RS)

| Feature             | Jersey (JAX-RS)                               |
| ------------------- | --------------------------------------------- |
| **Style**           | Annotation-based (`@Path`, `@GET`, etc.)      |
| **Spec Compliance** | ✅ Yes (Jakarta RESTful Web Services spec)     |
| **Learning Curve**  | Moderate                                      |
| **Container**       | Servlet-based or embedded (Grizzly, Jetty)    |
| **JSON Support**    | Yes (with Jackson or MOXy)                    |
| **Use Case**        | Standard REST APIs with strong spec alignment |

---

### 🔷 2. **Spring Boot (Spring MVC / WebFlux)**

| Feature             | Spring Boot                                         |
| ------------------- | --------------------------------------------------- |
| **Style**           | Annotation-based (`@RestController`, `@GetMapping`) |
| **Spec Compliance** | ❌ Not based on JAX-RS, uses its own abstractions    |
| **Learning Curve**  | Easy to moderate                                    |
| **Container**       | Embedded (Tomcat, Jetty, Netty)                     |
| **JSON Support**    | Built-in with Jackson                               |
| **Use Case**        | Most popular for microservices and REST APIs        |

---

### 🔷 3. **Quarkus**

| Feature             | Quarkus                                            |
| ------------------- | -------------------------------------------------- |
| **Style**           | Supports both JAX-RS (via RESTEasy) and Spring API |
| **Spec Compliance** | ✅ Yes (JAX-RS via RESTEasy)                        |
| **Learning Curve**  | Moderate                                           |
| **Container**       | Native or JVM, optimized for GraalVM               |
| **JSON Support**    | Yes                                                |
| **Use Case**        | Cloud-native, fast boot time, great for containers |

---

### 🔷 4. **Micronaut**

| Feature             | Micronaut                              |
| ------------------- | -------------------------------------- |
| **Style**           | Annotation-based (like Spring)         |
| **Spec Compliance** | ❌ Not JAX-RS                           |
| **Learning Curve**  | Moderate                               |
| **Container**       | No servlet container needed            |
| **JSON Support**    | Yes                                    |
| **Use Case**        | Microservices, low-memory environments |

---

### ⚖️ Summary Table

| Feature / Framework  | Jersey  | Spring Boot | Quarkus | Micronaut |
| -------------------- | ------- | ----------- | ------- | --------- |
| **JAX-RS compliant** | ✅ Yes   | ❌ No        | ✅ Yes   | ❌ No      |
| **Ease of use**      | Medium  | ✅ Easy      | Medium  | Medium    |
| **Startup time**     | Medium  | Slow (JVM)  | ✅ Fast  | ✅ Fast    |
| **Cloud-native**     | ❌ Basic | ✅ Yes       | ✅ Yes   | ✅ Yes     |
| **Built-in DI**      | HK2     | ✅ Spring    | ✅ CDI   | ✅ Own DI  |

---

**Bottom line**:

* ✅ **Use Jersey**: if you want **pure JAX-RS** spec-compliant REST APIs.
* ✅ **Use Spring Boot**: if you want **wide community support**, simplicity, and built-in everything.
* ✅ **Use Quarkus**: if you want **cloud-native and GraalVM native-image** support.
* ✅ **Use Micronaut**: if you need **low memory and fast cold starts**.

Would you like help choosing one based on your project goals?


