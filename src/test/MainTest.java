
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.driver.v1.*;

import java.util.Arrays;
import java.util.List;

public class MainTest {

    private static Main example = new Main();

    @BeforeClass
    public static void fillDB(){
        example = new Main();

        try (Session session = Main.driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (n)-[r]-() DELETE n,r;");
                tx.success();
            }
        }

        Main.addUser("Tom", 21, 1, "male", new String[]{"Hello Word!", "Raund", "Versus", "Spoon", "Cup"});
        Main.addUser("Lucy", 23, 2, "female", new String[]{"Eat", "Instagram", "Village"});
        Main.addUser("Max", 20, 3, "male", new String[]{"Cars my love", "Just do it"});
        Main.addUser("Olivia", 19, 4, "female", new String[]{"Mousee kek", "I don'n know", "Happy birthday"});
        Main.addUser("Lucas", 23, 5, "male", new String[]{"Lamp don't blink", "Calendar"});
        Main.addUser("Victor", 5, 6, "male", new String[]{"Programming", "Java", "Python", "Stack", "Linux"});
        Main.addUser("Logan", 22, 7, "male", new String[]{"Rochomsha", "Water", "Orange"});
        Main.addUser("Torvald", 20, 8, "male", new String[]{"Creator", "Operation system"});
        Main.addUser("Bob", 16,9,"male", new String[]{"Laptop", "Door", "Phone"});
        Main.addUser("Alex", 11,10,"male", new String[]{"Fireplace", "Moon", "Star", "Kiev"});

        Main.addGroup("Programing", 11);
        Main.addGroup("KPILive", 12);
        Main.addGroup("Music", 13);

        List<Integer> friendFirstList = Arrays.asList(1,2,1,4,5,1,6,2,10,10,10,10,9,9,9);
        List<Integer> friendSecondList = Arrays.asList(3,3,8,5,7,6,2,6,1,8,9,2,1,5,4);

        for (int x = 0; x<15; x++){
            Main.addFriend(friendFirstList.get(x), friendSecondList.get(x));
        }

        List<Integer> groupFirstList = Arrays.asList(11,11,11,11,11,11,11,12,12,12,12,13,13,13,13,13);
        List<Integer> groupSecondList = Arrays.asList(10,2,3,4,6,7,8,1,6,4,7,10,9,8,3,5);
        for (int x = 0; x<16; x++){
            Main.addSubscriber(groupFirstList.get(x), groupSecondList.get(x));
        }
    }

    @Test
    public void getTopName() {
        String expected = "[[\"Alex\"], [\"Bob\"], [\"Logan\"], [\"Lucas\"], [\"Lucy\"], [\"Max\"], [\"Olivia\"], [\"Tom\"], [\"Torvald\"], [\"Victor\"]]";
        Assert.assertEquals(example.getTopName(), expected);
    }

    @Test
    public void getNameMale() {
        String expected = "[[\"Lucas\", 23], [\"Logan\", 22], [\"Tom\", 21], [\"Max\", 20], [\"Torvald\", 20], [\"Bob\", 16], [\"Alex\", 11], [\"Victor\", 5]]";
        Assert.assertEquals(example.getNameMale(), expected);
    }

    @Test
    public void getFriends() {
        String expected = "[[\"Lucy\"], [\"Tom\"]]";
        Assert.assertEquals(example.getFriends("Victor"), expected);
    }

    @Test
    public void getFriendsFriends() {
        String expected = "[[\"Alex\"], [\"Alex\"], [\"Bob\"], [\"Max\"], [\"Max\"], [\"Torvald\"]]";
        Assert.assertEquals(example.getFriendsFriends("Victor"), expected);
    }

    @Test
    public void getNameAndCountFriends() {
        String expected = "[[\"Alex\", 4], [\"Bob\", 4], [\"Logan\", 1], [\"Lucas\", 3], [\"Lucy\", 3], [\"Max\", 2], [\"Olivia\", 2], [\"Tom\", 5], [\"Torvald\", 2], [\"Victor\", 2]]";
        Assert.assertEquals(example.getNameAndCountFriends(), expected);
    }

    @Test
    public void getGroups() {
        String expected = "[[\"KPILive\"], [\"Music\"], [\"Programing\"]]";
        Assert.assertEquals(example.getGroups(), expected);
    }

    @Test
    public void getGroupsByName() {
        String expected = "[[\"KPILive\"], [\"Programing\"]]";
        Assert.assertEquals(example.getGroupsByName("Victor"), expected);
    }

    @Test
    public void getGroupsAndMemberCount() {
        String expected = "[[\"Programing\", 7], [\"Music\", 5], [\"KPILive\", 4]]";
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
        String expected = "[[\"Alex\", []], [\"Bob\", []], [\"Logan\", []], [\"Lucas\", [\"Lamp don't blink\"]], [\"Lucy\", []], [\"Max\", [\"Cars my love\"]], [\"Olivia\", [\"I don'n know\", \"Happy birthday\"]], [\"Tom\", [\"Hello Word!\"]], [\"Torvald\", [\"Operation system\"]], [\"Victor\", [\"Programming\"]]]";
        Assert.assertEquals(example.getPostsGtNum(10), expected);
    }

    @Test
    public void getNameAndPosts() {
        String expected = "[[\"Tom\", 5], [\"Victor\", 5], [\"Alex\", 4], [\"Bob\", 3], [\"Logan\", 3], [\"Lucy\", 3], [\"Olivia\", 3], [\"Lucas\", 2], [\"Max\", 2], [\"Torvald\", 2]]";
        Assert.assertEquals(example.getNameAndPosts(), expected);
    }

    @Test
    public void getPostsFriendsFriends() {
        String expected = "[[\"Alex\", [\"Fireplace\", \"Moon\", \"Star\", \"Kiev\"]], [\"Alex\", [\"Fireplace\", \"Moon\", \"Star\", \"Kiev\"]], [\"Bob\", [\"Laptop\", \"Door\", \"Phone\"]], [\"Max\", [\"Cars my love\", \"Just do it\"]], [\"Max\", [\"Cars my love\", \"Just do it\"]], [\"Torvald\", [\"Creator\", \"Operation system\"]]]";
        Assert.assertEquals(example.getPostsFriendsFriends("Victor"), expected);
    }

    @Test
    public void getPostsSort() {
        String expected = "[[\"Lucas\", 12], [\"Olivia\", 12], [\"Max\", 11], [\"Torvald\", 11], [\"Logan\", 6], [\"Lucy\", 6], [\"Tom\", 6], [\"Victor\", 6], [\"Alex\", 5], [\"Bob\", 5]]";
        Assert.assertEquals(example.getPostsSort(), expected);
    }

}