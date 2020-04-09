package il.ac.afeka.tomco.a10242_cookeat.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable{

    private String documentId;
    private String title;
    private String ImagePath;

    public CategoryModel() {
    }

    public CategoryModel(String categoryId, String title, String imagePath) {
        this.documentId = categoryId;
        this.title = title;
        this.ImagePath = imagePath;
    }

    protected CategoryModel(Parcel in) {
        documentId = in.readString();
        title = in.readString();
        ImagePath = in.readString();
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        this.ImagePath = imagePath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeString(title);
        dest.writeString(ImagePath);
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", ImagePath='" + ImagePath + '\'' +
                '}';
    }


}
