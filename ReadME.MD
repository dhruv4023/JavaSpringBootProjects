# 📘 CodeNote – AI-Powered Developer Notes Platform

CodeNote is a Spring Boot–based backend system that allows developers to create structured notes from GitHub code references and enhance them with AI-powered insights like summaries, tags, and contextual suggestions.

---

## 🚀 Features

* 🔗 Create notes using **GitHub permalink**
* 📓 Organize notes into **Notebooks**
* 🧠 Automatic AI processing:

  * Summary generation
  * Smart tags
  * Improved descriptions
  * Context-aware suggestions
* ⚡ Event-driven architecture using **Inngest Events**
* 🔍 Contextual search using **Vector Search**
* 💬 Chat with your notebook (AI conversation endpoint)

---

## 🏗️ Architecture Overview

1. **User creates a CodeNote**

   * Inputs:

     * Title
     * GitHub permalink
     * Description

2. **Notebook System**

   * Users can group notes into notebooks

3. **Event Trigger (inngest)**

   * On note creation → triggers ingestion pipeline

4. **Processing Pipeline**

   * Fetch code/content from permalink
   * Generate embeddings
   * Store in vector database
   * Call AI model for:

     * Summary
     * Tags
     * Suggestions

5. **AI Layer**

   * Uses **Gemini Text Generative Model**
   * Uses **Vector Search** for contextual understanding

6. **Conversation API**

   * Users can chat with their notebook
   * Responses are context-aware based on stored notes

---

## 🛠️ Tech Stack

* **Backend:** Spring Boot
* **Build Tool:** Maven
* **Java Version:** 17
* **AI Model:** Gemini (Text Generation)
* **Search:** MongoDB Vector Database (for semantic retrieval)
* **Event System:** Inngest Events
* **Database:** MySql

---

## ⚙️ Getting Started

### Prerequisites

* Java 17
* Maven 3.x

---

### 📦 Installation

1. **Clone the Repository**

```bash
git clone https://github.com/dhruv4023/AuthenticationServerJava
cd AuthenticationServerJava
```

2. **Install Dependencies**

```bash
chmod +x setup.sh
./setup.sh
```
It will ask for jdk selection, select JSK 17. it will create vscode settings as per your JDK path and install dependency.

3. **Run the Application**

```bash
mvn spring-boot:run
```

---

## 📡 API Overview

### 📝 Create Code Note

* Adds a new note with GitHub permalink

### 📚 Create Notebook

* Organize notes into collections

### 🤖 AI Processing (Async)

* Triggered automatically on note creation

### 💬 Chat with Notebook

* Ask questions based on stored notes
* Context-aware AI responses

---

## 🔄 Workflow Example

1. User creates a note with GitHub permalink / github PR link
2. Inngest event is triggered
3. Backend ingests code data
4. AI generates:

   * Summary
   * Tags
   * Suggestions
5. Data stored in vector DB
6. User can query or chat with notebook

---

## 👨‍💻 Author

**Dhruv Patel**

---
