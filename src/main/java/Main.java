import org.neo4j.driver.v1.*;
import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

public class Main
{
    public static Driver driver;

    public static void addUser(String name, int age, int id, String sex, String[] posts) {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("CREATE (a:User {name: {name}, age : {age}, " +
                                "id : {id}, sex: {sex}, posts: {posts}})",
                        parameters("name", name, "age", age, "id", id, "sex", sex, "posts", posts));
                tx.success();
            }
        }
    }

    public static void addGroup(String name, int id) {
        try (Session session = driver.session()){
            try (Transaction tx = session.beginTransaction()){
                tx.run("CREATE (a:Group {name: {name}, id : {id}})",
                        parameters("name", name, "id", id));
                tx.success();
            }
        }
    }

    public static void addFriend(int firstUserID, int secondUserID) {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (a{id: {fUser} }),(b{id: {sUser} }) MERGE (a)-[r:Friend]-(b)",
                        parameters("fUser", firstUserID, "sUser",secondUserID));
                tx.success();
            }
        }
    }

    public static void addSubscriber(int groupID, int userID) {
        try (Session session = driver.session()){
            try (Transaction tx = session.beginTransaction()){
                tx.run("MATCH (a{id: {user} }),(b{id: {group} }) MERGE (a)-[r: Subscriber]-(b)",
                        parameters("group", groupID, "user",userID));
                tx.success();
            }
        }
    }

    public String getTopName(){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User) RETURN n.name ORDER BY n.name");
            return resultToString(result);
        }
    }

    public String getNameMale(){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User {sex:\"male\"}) " +
                    "RETURN n.name, n.age ORDER BY n.age DESC, n.name");
            return resultToString(result);
        }
    }

    public String getFriends(String name){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (node:User {name:$param})-[:Friend]-(n) " +
                            "RETURN n.name ORDER BY n.name",
                    parameters("param", name));
            return resultToString(result);
        }
    }

    public String getFriendsFriends(String name){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (node:User {name:$param})-[:Friend*2]-(f) " +
                            "RETURN f.name ORDER BY f.name",
                    parameters("param", name));
            return resultToString(result);
        }
    }

    public String getNameAndCountFriends(){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User)-[:Friend]-(f) " +
                    "RETURN n.name, COUNT(f) AS counter ORDER BY n.name");
            return resultToString(result);
        }
    }

    public String getGroups(){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (g:Group) RETURN g.name ORDER BY g.name");
            return resultToString(result);
        }
    }

    public String getGroupsByName(String name) {
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (g:Group)-[:Subscriber]-(u:User{name:$param}) " +
                            "RETURN g.name ORDER BY g.name",
                    parameters("param", name));
            return resultToString(result);
        }
    }

    public String getGroupsAndMemberCount(){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (g:Group)-[:Subscriber]-(u:User) " +
                    "RETURN g.name, COUNT(u) ORDER BY COUNT(u) DESC");
            return resultToString(result);
        }
    }

    public String getCountGroupsFriendsFriends(String name){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (u:User {name:$param})-[:Friend*2]-(f:User)-[:Subscriber]-(g:Group) " +
                            "RETURN COUNT(g)",
                    parameters("param", name));
            return resultToString(result);
        }
    }

    public String getPosts(String name){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User {name:$param}) RETURN n.posts",
                    parameters("param", name));
            return resultToString(result);
        }
    }

    public String getPostsGtNum(int num){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User) " +
                            "RETURN n.name, FILTER(row in n.posts " +
                            "WHERE LENGTH(row)>{param}) ORDER BY n.name",
                    parameters("param", num));
            return resultToString(result);
        }
    }

    public String getNameAndPosts(){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User) RETURN n.name, SIZE(n.posts) " +
                    "ORDER BY size(n.posts) DESC, n.name");
            return resultToString(result);
        }
    }

    public String getPostsSort(){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (p:User) " +
                    "WITH (reduce(total = 0, row in p.posts | total + length(row)))/size(p.posts) " +
                    "AS num, p.name AS name " +
                    "RETURN name, num ORDER BY num DESC, name");
            return resultToString(result);
        }
    }

    public String getPostsFriendsFriends(String name){
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User {name:$param})-[:Friend*2]-(f:User) " +
                            "RETURN f.name, f.posts ORDER BY f.name",
                    parameters("param", name));
            return resultToString(result);
        }
    }

    public static String resultToString(StatementResult result){
        List<String> resultList = new ArrayList<>();
        while (result.hasNext()){
            Record record = result.next();
            resultList.add(record.values().toString());
        }
        return resultList.toString();
    }

    public Main()
    {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4j"));
    }

}