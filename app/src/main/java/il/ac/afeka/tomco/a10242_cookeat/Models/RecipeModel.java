package il.ac.afeka.tomco.a10242_cookeat.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RecipeModel implements Parcelable {
    private String title;
    private String uploadingUserID;
    private String documentId;
    private String imageUrl;
    private int readyInMinutes;
    private List<String> ingredientsList = new ArrayList<String>();
    private List<String> equipmentList = new ArrayList<String>();
    private List<String> instructionList = new ArrayList<String>();
    private String cuisineID;

    public String getCuisineID() {
        return cuisineID;
    }

    public void setCuisineID(String cuisineID) {
        this.cuisineID = cuisineID;
    }

    public RecipeModel() {
    }

    public RecipeModel(String title, String uploadingUserID, String documentId, String imageUrl, int readyInMinutes, List<String> ingredientsList, List<String> EQUIPMENT_LIST, List<String> INSTRUCTION_LIST, String cuisineId) {
        this.title = title;
        this.uploadingUserID = uploadingUserID;
        this.documentId = documentId;
        this.imageUrl = imageUrl;
        this.readyInMinutes = readyInMinutes;
        this.ingredientsList = ingredientsList;
        this.equipmentList = EQUIPMENT_LIST;
        this.instructionList = INSTRUCTION_LIST;
        this.cuisineID = cuisineId;
    }

    private RecipeModel(Parcel in) {
        title = in.readString();
        uploadingUserID = in.readString();
        documentId = in.readString();
        cuisineID = in.readString();
        imageUrl = in.readString();
        readyInMinutes = in.readInt();
        ingredientsList.addAll(in.readArrayList(String.class.getClassLoader()));
        equipmentList.addAll(in.readArrayList(String.class.getClassLoader()));
        instructionList.addAll(in.readArrayList(String.class.getClassLoader()));
    }

    public static final Creator<RecipeModel> CREATOR = new Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel in) {
            return new RecipeModel(in);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
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

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getIngredientsList() {
        return ingredientsList;
    }

    public List<String> getInstructionList() {
        return instructionList;
    }

    public String getUploadingUserID() {
        return uploadingUserID;
    }

    public void setUploadingUserID(String uploadingUserID) {
        this.uploadingUserID = uploadingUserID;
    }

    public void addIngredient(String ing) {
        ingredientsList.add(ing);
    }

    public void addInstruction(String inst) {
        instructionList.add(inst);
    }

    public List<String> getEquipmentList() {
        return equipmentList;
    }

    public void setIngredientsList(List<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setEquipmentList(List<String> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public void setInstructionList(List<String> instructionList) {
        this.instructionList = instructionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(uploadingUserID);
        dest.writeString(documentId);
        dest.writeString(cuisineID);
        dest.writeString(imageUrl);
        dest.writeInt(readyInMinutes);
        dest.writeStringList(ingredientsList);
        dest.writeStringList(equipmentList);
        dest.writeStringList(instructionList);
    }

    public void addEquipment(String equip) {
        equipmentList.add(equip);
    }
}
