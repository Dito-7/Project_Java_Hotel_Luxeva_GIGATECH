package DataModel;

public class room {
    protected int room_id;
    protected RoomType type;
    protected int price;
    protected String Description;

    // Constructor
    public room(int room_id, RoomType type, int price, String Description) {
        this.room_id = room_id;
        this.type = type;
        this.price = price;
        this.Description = Description;
    }

    // Getter and Setter methods
    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public enum RoomType {
        Standard,
        Superior,
        Family,
        Deluxe,
        Suite
    }
}
