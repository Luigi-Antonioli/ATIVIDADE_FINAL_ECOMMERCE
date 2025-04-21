# Guia Prático JDBC com PostgreSQL em Java

## 1. Adicione o Driver JDBC do PostgreSQL

Se estiver usando **Maven**, adicione ao `pom.xml`:

```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.7.1</version>
</dependency>
```

Se estiver sem Maven, baixe o `.jar` em: [https://jdbc.postgresql.org/download/](https://jdbc.postgresql.org/download/)  
E adicione ao **classpath** do seu projeto.

---

## 2. Criar Conexão com PostgreSQL

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoPostgres {
    public static Connection conectar() throws SQLException {
        // Parâmetros de conexão
        String url = "jdbc:sqlite:database.sqlite";

        // Tentativa de conexão
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Conectado com sucesso ao SQLite!");
            } else {
                System.out.println("Falha na conexão.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
        
        return conn;
    }
}
```

---

## 3. Imports para comandos SQL

```java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
```

---

## 4. Executar Comandos SQL

### ✅ Criar Tabela

```java
String sql = "CREATE TABLE IF NOT EXISTS alunos (id SERIAL PRIMARY KEY, nome VARCHAR(100))";

try (Statement stmt = connection.createStatement()) {
    stmt.execute(sql);
    System.out.println("Tabela criada com sucesso.");
}
```

### ➕ Inserir Dados

```java
String sql = "INSERT INTO alunos (nome) VALUES (?)";

try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    pstmt.setString(1, "João");
    pstmt.executeUpdate();
    System.out.println("Dados inseridos.");
}
```

### 🔍 Consultar Dados

```java
String sql = "SELECT * FROM alunos";

try (Statement stmt = connection.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {

    while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        System.out.println("ID: " + id + " - Nome: " + nome);
    }
}
```

### ✏️ Atualizar Dados

```java
String sql = "UPDATE alunos SET nome = ? WHERE id = ?";

try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    pstmt.setString(1, "Maria");
    pstmt.setInt(2, 1);
    int linhasAfetadas = pstmt.executeUpdate();
    System.out.println("Atualizações: " + linhasAfetadas);
}
```

### ❌ Deletar Dados

```java
String sql = "DELETE FROM alunos WHERE id = ?";

try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    pstmt.setInt(1, 1);
    int linhasAfetadas = pstmt.executeUpdate();
    System.out.println("Linhas deletadas: " + linhasAfetadas);
}
```

---

## 🧹 Fechar Conexão

```java
if (connection != null && !connection.isClosed()) {
    connection.close();
    System.out.println("Conexão encerrada.");
}
```
