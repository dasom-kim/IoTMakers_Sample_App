package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DeviceNew {
    private List<TagStrm> tagStrmList;

    @SerializedName("sequence")
    private String sequence;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("used")
    private String used;

    @SerializedName("published")
    private String published;

    @SerializedName("status")
    private String status;

    @SerializedName("authenticationType")
    private String authenticationType;

    @SerializedName("authenticationKey")
    private String authenticationKey;

    @SerializedName("authenticationCertificate")
    private AuthenticationCertificate authenticationCertificate;

    @SerializedName("connectionId")
    private String connectionId;

    @SerializedName("connectionType")
    private String connectionType;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("imageFileId")
    private String imageFileId;

    @SerializedName("creator")
    private String creator;

    @SerializedName("createdOn")
    private String createdOn;

    @SerializedName("modifier")
    private String modifier;

    @SerializedName("modifiedOn")
    private String modifiedOn;

    @SerializedName("target")
    private Target target;

    @SerializedName("model")
    private Model model;

    @SerializedName("categories")
    private ArrayList<Categories> categories;

    @SerializedName("sensingTags")
    private ArrayList<SensingTags> sensingTags;

    public List<TagStrm> getTagStrmList() {
        return tagStrmList;
    }

    public void setTagStrmList(List<TagStrm> tagStrmList) {
        this.tagStrmList = tagStrmList;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public AuthenticationCertificate getAuthenticationCertificate() {
        return authenticationCertificate;
    }

    public void setAuthenticationCertificate(AuthenticationCertificate authenticationCertificate) {
        this.authenticationCertificate = authenticationCertificate;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageFileId() {
        return imageFileId;
    }

    public void setImageFileId(String imageFileId) {
        this.imageFileId = imageFileId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ArrayList<Categories> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categories> categories) {
        this.categories = categories;
    }

    public ArrayList<SensingTags> getSensingTags() {
        return sensingTags;
    }

    public void setSensingTags(ArrayList<SensingTags> sensingTags) {
        this.sensingTags = sensingTags;
    }

    @Override
    public String toString() {
        return "DeviceNew{" +
                "sequence='" + sequence + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", used='" + used + '\'' +
                ", published='" + published + '\'' +
                ", status='" + status + '\'' +
                ", authenticationType='" + authenticationType + '\'' +
                ", authenticationKey='" + authenticationKey + '\'' +
                ", authenticationCertificate={}" + '\'' +
                ", connectionId='" + connectionId + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageFileId='" + imageFileId + '\'' +
                ", creator='" + creator + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", modifier='" + modifier + '\'' +
                ", modifiedOn='" + modifiedOn + '\'' +
                ", target={}" + '\'' +
                ", model={}" + '\'' +
                ", categories={}" + '\'' +
                ", sensingTags={}" +
                '}';
    }
}
