package kr.startoff.backend.domain.post.domain;

public enum Category {
	STUDY, PROJECT;

	public static boolean isCategory(String c) {
		Category[] categories = Category.values();
		for (Category category : categories) {
			if(category.toString().equals(c.toUpperCase())){
				return true;
			}
		}
		return false;
	}
}
