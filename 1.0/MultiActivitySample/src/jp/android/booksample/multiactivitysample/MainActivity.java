package jp.android.booksample.multiactivitysample;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 *���C���̃A�N�e�B�r�e�B�N���X
 * @author �͍s
 *
 */
public class MainActivity extends ListActivity {

	/**
	 * �f�[�^�x�[�X�e�[�u������
	 */
	public static final String TABLE_NAME = "memo_data";

	/**
	 * �f�[�^�x�[�X�e�[�u���́uID�v�������L�[��
	 */

	public static final String COLUMN_ID = "_id";

	/**
	 * �f�[�^�x�[�X�e�[�u���́u�^�C�g���v�������L�[��
	 */
	public static final String COLUMN_TITLE = "title";
	/**
	 * �f�[�^�x�[�X�e�[�u���́u�{���v�������L�[��
	 */
	public static final String COLUMN_BODY = "body";

	/**
	 * SQL��where����(ID)
	 */
	private static final String SQL_WHERE_ID = COLUMN_ID + " = ?";

	/**
	 * �G�f�B�^��ʂƂ̘A���ɗp����C���e���g�Ɋ܂܂��AID�̃L�[
	 */
	public static final String EXTRA_ID = "id";

	/**
	 * �G�f�B�^��ʂƂ̘A���ɗp����C���e���g�Ɋ܂܂��A�^�C�g���̃L�[
	 */
	public static final String EXTRA_TITLE = "title";

	/**
	 * �G�f�B�^��ʂƂ̘A���ɗp����C���e���g�Ɋ܂܂��A�{���̃L�[
	 */
	public static final String EXTRA_BODY = "body";

	/**
	 * �G�f�B�^�Ńf�[�^�̒ǉ����s���ۂ̃��N�G�X�g�̃L�[
	 */
	private static final int REQUEST_ADD = 1;

	/**
	 * �G�f�B�^�Ńf�[�^�̕ҏW���s���ۂ̃��N�G�X�g�̃L�[
	 */
	private static final int REQUEST_EDIT = 2;

	/**
	 * �A�N�e�B�r�e�B�N�����ɌĂ΂��
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		// �f�[�^�x�[�X�N�G���̔��s
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

		// �\������l�̗p��
		String[] from = new String[] { COLUMN_TITLE, COLUMN_BODY };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, c, from, to, 0);

		setListAdapter(adapter);
	}

	/**
	 *
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
			SQLiteDatabase db = helper.getWritableDatabase();

			// �ǉ�����f�[�^��p�ӂ���
			ContentValues values = new ContentValues();
			values.put(COLUMN_TITLE, data.getStringExtra(EXTRA_TITLE));
			values.put(COLUMN_BODY, data.getStringExtra(EXTRA_BODY));


			switch (requestCode) {

			//�V�K���ڂ̒ǉ�
			case REQUEST_ADD:

				// �}���N�G���̑��M���s��
				db.insert(TABLE_NAME, null, values);
				break;

			//�������ڂ̕ύX
			case REQUEST_EDIT:

				long id = data.getLongExtra(EXTRA_ID, 0);

				// �X�V�N�G���̑��M
				String[] whereargs = new String[] { Long.toString(id) };
				db.update(TABLE_NAME, values, SQL_WHERE_ID, whereargs);
				break;


			default:
				break;
			}
			reloadCursor();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ���j���[���ڐݒ�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 *
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result;

		switch (item.getItemId()) {

		// �u�ǉ��v
		case R.id.operate_additem:

			// �ҏW��ʂ�\������
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), EditorActivity.class);

			// �A�N�e�B�r�e�B�̕\��
			startActivityForResult(intent, REQUEST_ADD);

			result = true;
			break;

		// �u�폜�v
		case R.id.operate_deleteitem:


			SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();

			// ���ڂ�����΍폜
			if (adapter.getCount() > 0) {
				// ���X�g��Ō�̍��ڂ�ID���擾����
				long id = adapter.getItemId(adapter.getCount() - 1);

				// �f�[�^�̍폜����
				MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
				SQLiteDatabase db = helper.getWritableDatabase();

				String[] whereargs = new String[] { Long.toString(id) };
				db.delete(TABLE_NAME, SQL_WHERE_ID, whereargs);

				reloadCursor();
			}
			else{
				showDialog(this, "�x��", "���ڂ����݂��܂���");
			}
			result = true;
			break;

		default:
			result = super.onOptionsItemSelected(item);
			break;
		}

		return result;
	}


	/**
	 * ���X�g�r���[�N���b�N���̃C�x���g
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// �ǂݍ��ݗp�f�[�^�x�[�X�I�u�W�F�N�g�̎擾
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = new String[] { COLUMN_TITLE, COLUMN_BODY };
		String[] whereargs = new String[] { Long.toString(id) };

		// �l�̎擾
		Cursor c = db.query(TABLE_NAME, columns, SQL_WHERE_ID, whereargs, null,
				null, null);
		c.moveToFirst();

		// �ҏW��ʂ�\��
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), EditorActivity.class);
		intent.putExtra(EXTRA_ID, id);

		// �f�[�^�x�[�X����擾�����l�̐ݒ�
		intent.putExtra(EXTRA_TITLE, c.getString(0));
		intent.putExtra(EXTRA_BODY, c.getString(1));

		// �A�N�e�B�r�e�B�̕\��
		startActivityForResult(intent, REQUEST_EDIT);
	}

	/**
	 * �ēǍ�
	 */
	private void reloadCursor() {
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		// �f�[�^�x�[�X�N�G���̔��s
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

		// �J�[�\���̕ύX
		SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
		adapter.swapCursor(c);
	}

	/**
	 * �_�C�A���O�\��
	 * @param context
	 * @param title
	 * @param text
	 */
	public static void showDialog(Context context, String title, String text) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(text);
		ad.setPositiveButton("OK", null);
		ad.show();
	}

}