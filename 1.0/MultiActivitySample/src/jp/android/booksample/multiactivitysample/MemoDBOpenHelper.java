package jp.android.booksample.multiactivitysample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * データベースを処理するためのヘルパークラス
 */
public class MemoDBOpenHelper extends SQLiteOpenHelper {

	/**
	 * データベース名称
	 */
	private static final String DATABASE_NAME = "MEMO_DATA";

	/**
	 * データベースにテーブルを作成するSQL文
	 */
	private static final String SQL_CREATE_TABLE = String
			.format("CREATE TABLE %1$s ( %2$s INTEGER PRIMARY KEY AUTOINCREMENT, %3$s TEXT NOT NULL, %4$s TEXT);",
					MainActivity.TABLE_NAME, MainActivity.COLUMN_ID,
					MainActivity.COLUMN_TITLE, MainActivity.COLUMN_BODY);

	/**
	 * データベースのバージョン
	 */
	private static final int DATABASE_VERSION = 1;

	/**
	 * コンストラクタ
	 * @param context
	 */
	public MemoDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * データベースを作成する処理( コンストラクタが呼び出された時点で存在しない場合)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	/**
	 * データベースを更新する処理
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}