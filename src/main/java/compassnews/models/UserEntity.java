package compassnews.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document(collection = "users")
public class UserEntity {

	@Id
	@Field("_id")
	private String _id;

	private String version;
	private String username;
	private Map<String, String> userDetails;
	private Map<String, String> userPreferences;
	private Map<String, String> userData;
	private Map<String, String> userLocation;
	private Map<String, String> subscription;
	private Map<String, String> articleLocation;
	private Map<String, List<String>> personalizationFilters;
	private String defaultCategory;
	private String defaultSubCategory;
	private UserType userType;
	private boolean validated;
	private boolean admin;
	private boolean external;

	public enum UserType {
		Active,
		Admin,
		Banned,
		Beta,
		Partial,
		Publisher,
		Student,
		Waiting
	}

	public enum PersonalizationFilterType {
		CATEGORIES,
		FEATURED_CATEGORIES,
		FEATURED_TAGS,
		TAGS
	}

	public static final Map<String, List<String>> DEFAULT_FILTERS = ImmutableMap.of(
			PersonalizationFilterType.CATEGORIES.name(), ImmutableList.of(),
			PersonalizationFilterType.FEATURED_CATEGORIES.name(), ImmutableList.of(),
			PersonalizationFilterType.FEATURED_TAGS.name(), ImmutableList.of(),
			PersonalizationFilterType.TAGS.name(), ImmutableList.of());

	public UserEntity() {}

	public UserEntity(String _id, String version, String username, Map<String, String> userDetails, Map<String, String> userPreferences, Map<String, String> userData, Map<String, String> userLocation, Map<String, String> subscription, Map<String, String> articleLocation, Map<String, List<String>> personalizationFilters, String defaultCategory, String defaultSubCategory, UserType userType, boolean validated, boolean admin, boolean external) {
		this._id = _id;
		this.version = version;
		this.username = username;
		this.userDetails = userDetails;
		this.userPreferences = userPreferences;
		this.userData = userData;
		this.userLocation = userLocation;
		this.subscription = subscription;
		this.articleLocation = articleLocation;
		this.personalizationFilters = personalizationFilters;
		this.defaultCategory = defaultCategory;
		this.defaultSubCategory = defaultSubCategory;
		this.userType = userType;
		this.validated = validated;
		this.admin = admin;
		this.external = external;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, String> getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(Map<String, String> userDetails) {
		this.userDetails = userDetails;
	}

	public Map<String, String> getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(Map<String, String> userPreferences) {
		this.userPreferences = userPreferences;
	}

	public Map<String, String> getUserData() {
		return userData;
	}

	public void setUserData(Map<String, String> userData) {
		this.userData = userData;
	}

	public Map<String, String> getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Map<String, String> userLocation) {
		this.userLocation = userLocation;
	}

	public Map<String, String> getSubscription() {
		return subscription;
	}

	public void setSubscription(Map<String, String> subscription) {
		this.subscription = subscription;
	}

	public Map<String, String> getArticleLocation() {
		return articleLocation;
	}

	public void setArticleLocation(Map<String, String> articleLocation) {
		this.articleLocation = articleLocation;
	}

	public Map<String, List<String>> getPersonalizationFilters() {
		return personalizationFilters;
	}

	public void setPersonalizationFilters(Map<String, List<String>> personalizationFilters) {
		this.personalizationFilters = personalizationFilters;
	}

	public String getDefaultCategory() {
		return defaultCategory;
	}

	public void setDefaultCategory(String defaultCategory) {
		this.defaultCategory = defaultCategory;
	}

	public String getDefaultSubCategory() {
		return defaultSubCategory;
	}

	public void setDefaultSubCategory(String defaultSubCategory) {
		this.defaultSubCategory = defaultSubCategory;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserEntity that = (UserEntity) o;

		return _id != null ? _id.equals(that._id) : that._id == null;
	}

	@Override
	public int hashCode() {
		return _id != null ? _id.hashCode() : 0;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("UserEntity{");
		sb.append("_id=").append(_id);
		sb.append(", version='").append(version).append('\'');
		sb.append(", username='").append(username).append('\'');
		sb.append(", userDetails=").append(userDetails);
		sb.append(", userPreferences=").append(userPreferences);
		sb.append(", userData=").append(userData);
		sb.append(", userLocation=").append(userLocation);
		sb.append(", subscription=").append(subscription);
		sb.append(", articleLocation=").append(articleLocation);
		sb.append(", personalizationFilters=").append(personalizationFilters);
		sb.append(", defaultCategory='").append(defaultCategory).append('\'');
		sb.append(", defaultSubCategory='").append(defaultSubCategory).append('\'');
		sb.append(", userType=").append(userType);
		sb.append(", validated=").append(validated);
		sb.append(", admin=").append(admin);
		sb.append(", external=").append(external);
		sb.append('}');
		return sb.toString();
	}
}

