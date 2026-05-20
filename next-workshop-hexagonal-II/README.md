# Streaming Music Platform (Hexagonal Architecture POC)

This project is a **Proof of Concept (POC)** of a music streaming platform backend inspired by services like Spotify.
It is designed for educational purposes, to demonstrate how to build a system using **Hexagonal Architecture (Ports &
Adapters)** with Java.

The main goal is to clearly separate **domain logic**, **application use cases**, and **infrastructure** concerns,
keeping the core business model independent of specific frameworks and technical details.

---

# ⚙️ Technical Characteristics

* ☕ **Java version:** 21
* 🧰 **Build tool:** Maven 3.9.5

---

# 🎓 Educational Context

This project is intended for a hands-on workshop delivered to students of the **Universidad Politécnica de Madrid (UPM)
**.

The workshop sessions take place on:

* 📅 **May 13**
* 📅 **May 20**

During these sessions, students will progressively build and understand a simplified structured backend system applying:

* Hexagonal Architecture principles
* Domain-Driven Design (DDD) basics
* Clean separation of concerns in Spring Boot applications

After the second session, the students will build their own application based on the practice exercise provided by the
instructor.

---

# 🧠 Architecture Overview

In the first approach, and following the principles of **Hexagonal architecture**, the code is structured based on the
domain concepts (user, library, catalog, etc) and for each of them into the three main layers:

```text
com.nextdigital.nextmusic
                 └──[domain-concept-1] #user
                    ├── domain
                    ├── application
                    └── infrastructure
                 ...
                 └──[domain-concept-n] #library, #catalog, #etc
                    ├── domain
                    ├── application
                    └── infrastructure
                 └──shared
                    ├── domain
                    ├── application
                    └── infrastructure
```

In the following iterations we will evolve the architecture to increase the modularity between the domain concepts as it
would happen in a real case scenario.
