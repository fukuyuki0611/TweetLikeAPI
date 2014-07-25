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
 *メインのアクティビティクラス
 * @author 範行
 *
 */
public class MainActivity extends ListActivity {

	/**
	 * データベーステーブル名称
	 */
	public static final String TABLE_NAME = "memo_data";

	/**
	 * データベーステーブルの「ID」を示すキー名
	 */

	public static final String COLUMN_ID = "_id";

	/**
	 * データベーステーブルの「タイトル」を示すキー名
	 */
	public static final String COLUMN_TITLE = "title";
	/**
	 * データベーステーブルの「本文」を示すキー名
	 */
	public static final String COLUMN_BODY = "body";

	/**
	 * SQLのwhere条件(ID)
	 */
	private static final String SQL_WHERE_ID = COLUMN_ID + " = ?";

	/**
	 * エディタ画面との連絡に用いるインテントに含まれる、IDのキー
	 */
	public static final String EXTRA_ID = "id";

	/**
	 * エディタ画面との連絡に用いるインテントに含まれる、タイトルのキー
	 */
	public static final String EXTRA_TITLE = "title";

	/**
	 * エディタ画面との連絡に用いるインテントに含まれる、本文のキー
	 */
	public static final String EXTRA_BODY = "body";

	/**
	 * エディタでデータの追加を行う際のリクエストのキー
	 */
	private static final int REQUEST_ADD = 1;

	/**
	 * エディタでデータの編集を行う際のリクエストのキー
	 */
	private static final int REQUEST_EDIT = 2;

	/**
	 * アクティビティ起動時に呼ばれる
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		// データベースクエリの発行
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

		// 表示する値の用意
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

			// 追加するデータを用意する
			ContentValues values = new ContentValues();
			values.put(COLUMN_TITLE, data.getStringExtra(EXTRA_TITLE));
			values.put(COLUMN_BODY, data.getStringExtra(EXTRA_BODY));


			switch (requestCode) {

			//新規項目の追加
			case REQUEST_ADD:

				// 挿入クエリの送信を行う
				db.insert(TABLE_NAME, null, values);
				break;

			//既存項目の変更
			case REQUEST_EDIT:

				long id = data.getLongExtra(EXTRA_ID, 0);

				// 更新クエリの送信
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
	 * メニュー項目設定
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

		// 「追加」
		case R.id.operate_additem:

			// 編集画面を表示する
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), EditorActivity.class);

			// アクティビティの表示
			startActivityForResult(intent, REQUEST_ADD);

			result = true;
			break;

		// 「削除」
		case R.id.operate_deleteitem:


			SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();

			// 項目があれば削除
			if (adapter.getCount() > 0) {
				// リスト上最後の項目のIDを取得する
				long id = adapter.getItemId(adapter.getCount() - 1);

				// データの削除処理
				MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
				SQLiteDatabase db = helper.getWritableDatabase();

				String[] whereargs = new String[] { Long.toString(id) };
				db.delete(TABLE_NAME, SQL_WHERE_ID, whereargs);

				reloadCursor();
			}
			else{
				showDialog(this, "警告", "項目が存在しません");
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
	 * リストビュークリック時のイベント
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// 読み込み用データベースオブジェクトの取得
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = new String[] { COLUMN_TITLE, COLUMN_BODY };
		String[] whereargs = new String[] { Long.toString(id) };

		// 値の取得
		Cursor c = db.query(TABLE_NAME, columns, SQL_WHERE_ID, whereargs, null,
				null, null);
		c.moveToFirst();

		// 編集画面を表示
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), EditorActivity.class);
		intent.putExtra(EXTRA_ID, id);

		// データベースから取得した値の設定
		intent.putExtra(EXTRA_TITLE, c.getString(0));
		intent.putExtra(EXTRA_BODY, c.getString(1));

		// アクティビティの表示
		startActivityForResult(intent, REQUEST_EDIT);
	}

	/**
	 * 再読込
	 */
	private void reloadCursor() {
		MemoDBOpenHelper helper = new MemoDBOpenHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		// データベースクエリの発行
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

		// カーソルの変更
		SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
		adapter.swapCursor(c);
	}

	/**
	 * ダイアログ表示
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