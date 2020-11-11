package app.paseico.data;


import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Route implements Parcelable {
    private String name;
    private String theme;   // TODO: The value of theme should be a constant.
    private double length;  // Meters.
    private double estimatedTime;  // Minutes

    // Points earned when the route is completed.
    private int rewardPoints;
    private List<PointOfInterest> pointsOfInterest;

    // It was decided that the reference to the Route author must be its database ID, rather than a Paseico.User.
    private String authorId;

    public Route() {
    }

    public Route(Parcel in) {
        readFromParcel(in);
    }

    public Route(String name, List<PointOfInterest> pointOfInterests, String authorId) {
        this.name = name;
        this.pointsOfInterest = pointOfInterests;
        this.authorId = authorId;
    }

    // TODO: Add the userId and pass it wherever this constructor is used.
    public Route(String name, String theme, double length, double estimatedTime, int rewardPoints, List<PointOfInterest> pointsOfInterest) {
        this.name = name;
        this.theme = theme;
        this.length = length;
        this.estimatedTime = estimatedTime;
        this.rewardPoints = rewardPoints;
        this.pointsOfInterest = pointsOfInterest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int points) {
        this.rewardPoints = points;
    }

    public List<PointOfInterest> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void setPointsOfInterest(List<PointOfInterest> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @Override
    public @NotNull String toString() {
        return "Route{" +
                "name='" + name + '\'' +
                "authorId=" + authorId + '\'' +
                ", theme='" + theme + '\'' +
                ", length=" + length +
                ", estimatedTime=" + estimatedTime +
                ", rewardsPoints=" + rewardPoints +
                ", pointsOfInterest=" + pointsOfInterest +
                '}';
    }

    private void readFromParcel(Parcel in) {
        this.name = in.readString();
        this.theme = in.readString();
        this.length = in.readDouble();
        this.estimatedTime = in.readDouble();
        this.rewardPoints = in.readInt();
        this.pointsOfInterest = in.readArrayList(PointOfInterest.class.getClassLoader());
    }

    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(theme);
        dest.writeDouble(length);
        dest.writeDouble(estimatedTime);
        dest.writeInt(rewardPoints);
        dest.writeList(pointsOfInterest);
    }
}
