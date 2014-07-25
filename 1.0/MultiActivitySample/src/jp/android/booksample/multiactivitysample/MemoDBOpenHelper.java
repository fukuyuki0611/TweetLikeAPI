package jp.android.booksample.multiactivitysample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * �f�[�^�x�[�X���������邽�߂̃w���p�[�N���X
 */
public class MemoDBOpenHelper extends SQLiteOpenHelper {

	/**
	 * �f�[�^�x�[�X����
	 */
	private static final String DATABASE_NAME = "MEMO_DATA";

	/**
	 * �f�[�^�x�[�X�Ƀe�[�u�����쐬����SQL��
	 */
	private static final String SQL_CREATE_TABLE = String
			.format("CREATE TABLE %1$s ( %2$s INTEGER PRIMARY KEY AUTOINCREMENT, %3$s TEXT NOT NULL, %4$s TEXT);",
					MainActivity.TABLE_NAME, MainActivity.COLUMN_ID,
					MainActivity.COLUMN_TITLE, MainActivity.COLUMN_BODY);

	/**
	 * �f�[�^�x�[�X�̃o�[�W����
	 */
	private static final int DATABASE_VERSION = 1;

	/**
	 * �R���X�g���N�^
	 * @param context
	 */
	public MemoDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * �f�[�^�x�[�X���쐬���鏈��( �R���X�g���N�^���Ăяo���ꂽ���_�ő��݂��Ȃ��ꍇ)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	/**
	 * �f�[�^�x�[�X���X�V���鏈��
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}