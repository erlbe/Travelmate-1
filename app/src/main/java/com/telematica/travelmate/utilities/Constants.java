package com.telematica.travelmate.utilities;


public class Constants {
    public static final String DB_TYPE = "API";
    public static final String SERVER_LINK = "http://192.168.71.2:8080/api";

    public final static String SERIALIZED_CATEGORY = "serialized_category";
    public static final String REALM_DATABASE = "travelmate.realm";
    public static final String SQLITE_DATABASE = "travelmate.sqlite";
    public static final String FIRST_RUN = "first_run";
    public static final String PREFERRED_EDITOR = "preferred_editor";
    public final static int LINED_EDITOR = 1;
    public final static int PLAIN_EDITOR = 2;
    public static final String ENTRY_ID = "ENTRY_ID";
    public static final String DEFAULT_CATEGORY = "General";
    public static final String APP_FOLDER = "TravelMate";
    public static final String BACKUP_FOLDER = "TravelMate/Backups";
    public static final String REALM_EXPORT_FILE_NAME = "realm_export";
    public static final String REALM_IMPORT_FILE_NAME = "realm_import";
    public static final String REALM_EXPORT_FILE_PATH = "realm_export_path";
    public static final String IS_DUAL_SCREEN = "is_dual_screen";
    public static final String SORT_PREFERENCE = "sort_PREFERENCE";

    public final static int ENTRY = 1;
    public final static int CATEGORY = 2;
    public static final int BACKUP = 3;
    public final static int SETTINGS = 4;
    public final static int ACCOUNT = 5;
    public final static int TABLE = 6;


    public final static int SORT_TITLE = 1;
    public final static int SORT_CATEGORY = 2;
    public final static int SORT_DATE = 3;

    public static final String ENTRIES_TABLE = "note";
    public static final String CATEGORY_TABLE = "category";

    //Async Query Ids
    public static final int INSERT_ENTRY = 1;
    public final static int DELETE_ENTRY = 2;
    public static final int INSERT_CATEGORY = 3;
    public final static int DELETE_CATEGORY = 4;
    public final static int QUERY_GET_ALL_CATEGORY = 5;

    public static final String COLUMN_ID = "_id";
    public final static String COLUMN_NAME = "name";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_MODIFIED_TIME = "modified_time";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_CREATED_TIME = "created_time";
    public static final String COLUMNS_CATEGORY_ID = "category_id";
    public static final String COLUMNS_TODO_LIST_ID = "todo_list_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_SORTED_ENTRIES = "sorted_entry";
    public static final String COLUMN_SORTED_TODOS = "sorted_todo";
    public static final String COLUMN_BACKUP_PROVIDER = "backup_provider";
    public static final String COLUMN_BACKUP_FILE_SIZE = "backup_file_size";
    public static final String COLUMN_IMAGE = "image";
    public final static String PREFERENCE_FILE = "preference_file";

    public final static int SELECT_PICTURE_REQUEST_CODE = 1000;



    public static final String[] COLUMNS_ENTRY = {

            Constants.COLUMN_ID,
            Constants.COLUMN_TITLE,
            Constants.COLUMN_CONTENT,
            Constants.COLUMN_IMAGE,
            Constants.COLUMN_CATEGORY_NAME,
            Constants.COLUMNS_CATEGORY_ID,
            Constants.COLUMN_CREATED_TIME,
            Constants.COLUMN_MODIFIED_TIME

    };

    public static final String[] COLUMNS_CATEGORY = {

            Constants.COLUMN_ID,
            Constants.COLUMN_NAME,
            Constants.COLUMN_CREATED_TIME,
            Constants.COLUMN_MODIFIED_TIME

    };

}
