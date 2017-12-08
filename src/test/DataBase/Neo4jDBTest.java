package DataBase;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.driver.v1.*;

import java.util.Arrays;
import java.util.List;

public class Neo4jDBTest {

    private static Neo4jDB example = new Neo4jDB();

    @BeforeClass
    public static void insertDataToDB(){
        example = new Neo4jDB();

        try (Session session = Neo4jDB.driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (n)-[r]-() DELETE n,r;");
                tx.success();
            }
        }

        Neo4jDB.addUser("Den", 21, 1, "male", new String[]{"Hello Word!", "Music", "ASM", "Android", "Cup"});
        Neo4jDB.addUser("Nata", 23, 2, "female", new String[]{"Eat", "Instagram", "Facebook"});
        Neo4jDB.addUser("Max", 20, 3, "male", new String[]{"100500", "Just do it"});
        Neo4jDB.addUser("Ann", 19, 4, "female", new String[]{"React", "JS", "Happy birthday"});
        Neo4jDB.addUser("Lucas", 23, 5, "male", new String[]{"RaspberryPI", "Arduino"});
        Neo4jDB.addUser("Victor", 5, 6, "male", new String[]{"Programming", "Java", "Python", "Stack", "Linux"});
        Neo4jDB.addUser("Kostya", 22, 7, "male", new String[]{"ASP.NET", "PHP", "Lisp"});
        Neo4jDB.addUser("Jimmy", 20, 8, "male", new String[]{"Creator", "Operation system"});
        Neo4jDB.addUser("Bob", 16,9,"male", new String[]{"Laptop", "IoT", "Phone"});
        Neo4jDB.addUser("Alex", 11,10,"male", new String[]{"MsSQL", "Moon", "Core", "RS485"});

        Neo4jDB.addGroup("Programing", 11);
        Neo4jDB.addGroup("KPILive", 12);
        Neo4jDB.addGroup("New movies", 13);

        List<Integer> friendFirstList = Arrays.asList(1,2,1,4,5,1,6,2,10,10,10,10,9,9,9);
        List<Integer> friendSecondList = Arrays.asList(3,3,8,5,7,6,2,6,1,8,9,2,1,5,4);

        for (int x = 0; x<15; x++){
            Neo4jDB.addFriend(friendFirstList.get(x), friendSecondList.get(x));
        }

        List<Integer> groupFirstList = Arrays.asList(11,11,11,11,11,11,11,12,12,12,12,13,13,13,13,13);
        List<Integer> groupSecondList = Arrays.asList(10,2,3,4,6,7,8,1,6,4,7,10,9,8,3,5);
        for (int x = 0; x<16; x++){
            Neo4jDB.addSubscriber(groupFirstList.get(x), groupSecondList.get(x));
        }
    }

    @Test
    public void getTopName() {
        String expected = "[[\"Alex\"], [\"Ann\"], [\"Bob\"], [\"Den\"], [\"Jimmy\"], [\"Kostya\"], [\"Lucas\"], [\"Max\"], [\"Nata\"], [\"Victor\"]]";
        Assert.assertEquals(example.getTopName(), expected);
    }

    @Test
    public void getNameMale() {
        String expected = "[[\"Lucas\", 23], [\"Kostya\", 22], [\"Den\", 21], [\"Jimmy\", 20], [\"Max\", 20], [\"Bob\", 16], [\"Alex\", 11], [\"Victor\", 5]]";
        Assert.assertEquals(example.getNameMale(), expected);
    }

    @Test
    public void getFriends() {
        String expected = "[[\"Den\"], [\"Nata\"]]";
        Assert.assertEquals(example.getFriends("Victor"), expected);
    }

    @Test
    public void getFriendsFriends() {
        String expected = "[[\"Alex\"], [\"Alex\"], [\"Bob\"], [\"Jimmy\"], [\"Max\"], [\"Max\"]]";
        Assert.assertEquals(example.getFriendsFriends("Victor"), expected);
    }

    @Test
    public void getNameAndCountFriends() {
        String expected = "[[\"Alex\", 4], [\"Ann\", 2], [\"Bob\", 4], [\"Den\", 5], [\"Jimmy\", 2], [\"Kostya\", 1], [\"Lucas\", 3], [\"Max\", 2], [\"Nata\", 3], [\"Victor\", 2]]";
        Assert.assertEquals(example.getNameAndCountFriends(), expected);
    }

    @Test
    public void getGroups() {
        String expected = "[[\"KPILive\"], [\"New movies\"], [\"Programing\"]]";
        Assert.assertEquals(example.getGroups(), expected);
    }

    @Test
    public void getGroupsByName() {
        String expected = "[[\"KPILive\"], [\"Programing\"]]";
        Assert.assertEquals(example.getGroupsByName("Victor"), expected);
    }

    @Test
    public void getGroupsAndMemberCount() {
        String expected = "[[\"Programing\", 7], [\"New movies\", 5], [\"KPILive\", 4]]";
        Assert.assertEquals(example.getGroupsAndMemberCount(), expected);
    }

    @Test
    public void getCountGroupsFriendsFriends() {
        String expected = "[[11]]";
        Assert.assertEquals(example.getCountGroupsFriendsFriends("Victor"), expected);
    }

    @Test
    public void getPosts() {
        String expected = "[[[\"Programming\", \"Java\", \"Python\", \"Stack\", \"Linux\"]]]";
        Assert.assertEquals(example.getPosts("Victor"), expected);
    }

    @Test
    public void getPostsGtNum() {
        String expected = "[[\"Alex\", []], [\"Ann\", [\"Happy birthday\"]], [\"Bob\", []], [\"Den\", [\"Hello Word!\"]], [\"Jimmy\", [\"Operation system\"]], [\"Kostya\", []], [\"Lucas\", [\"RaspberryPI\"]], [\"Max\", []], [\"Nata\", []], [\"Victor\", [\"Programming\"]]]";
        Assert.assertEquals(example.getPostsGtNum(10), expected);
    }

    @Test
    public void getNameAndPosts() {
        String expected = "[[\"Den\", 5], [\"Victor\", 5], [\"Alex\", 4], [\"Ann\", 3], [\"Bob\", 3], [\"Kostya\", 3], [\"Nata\", 3], [\"Jimmy\", 2], [\"Lucas\", 2], [\"Max\", 2]]";
        Assert.assertEquals(example.getNameAndPosts(), expected);
    }

    @Test
    public void getPostsFriendsFriends() {
        String expected = "[[\"Alex\", [\"MsSQL\", \"Moon\", \"Core\", \"RS485\"]], [\"Alex\", [\"MsSQL\", \"Moon\", \"Core\", \"RS485\"]], [\"Bob\", [\"Laptop\", \"IoT\", \"Phone\"]], [\"Jimmy\", [\"Creator\", \"Operation system\"]], [\"Max\", [\"100500\", \"Just do it\"]], [\"Max\", [\"100500\", \"Just do it\"]]]";
        Assert.assertEquals(example.getPostsFriendsFriends("Victor"), expected);
    }

    @Test
    public void getPostsSort() {
        String expected = "[[\"Jimmy\", 11], [\"Lucas\", 9], [\"Max\", 8], [\"Ann\", 7], [\"Nata\", 6], [\"Victor\", 6], [\"Den\", 5], [\"Alex\", 4], [\"Bob\", 4], [\"Kostya\", 4]]";
        Assert.assertEquals(example.getPostsSort(), expected);
    }

}

