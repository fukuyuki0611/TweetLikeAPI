package jp.android.booksample.multiactivitysample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * ���ڕҏW�A�N�e�B�r�e�B
 * @author �͍s
 *
 */
public class EditorActivity extends Activity implements OnClickListener {

	/**
	 * �ҏW���̍���ID
	 */
	private long editorId;

	/**
	 *
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		// �C�x���g�n���h���̐ݒ�
		Button operate_save = (Button) findViewById(R.id.operate_save);
		operate_save.setOnClickListener(this);

		// Extra���w�肳�ꂽ�ꍇ�A���̐ݒ�𔽉f����
		Intent intent = getIntent();

		if (intent.hasExtra(MainActivity.EXTRA_ID)) {
			EditText editor_title = (EditText) findViewById(R.id.editor_title);
			EditText editor_body = (EditText) findViewById(R.id.editor_body);

			//ID,�^�C�g���A�{��
			editorId = intent.getLongExtra(MainActivity.EXTRA_ID, 0);

			editor_title.setText(intent
					.getStringExtra(MainActivity.EXTRA_TITLE));
			editor_body.setText(intent.getStringExtra(MainActivity.EXTRA_BODY));
		}
		else {
			editorId = -1;
		}
	}

	/**
	 * �N���b�N���̋���
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		//�ۑ�
		case R.id.operate_save:
			EditText editor_title = (EditText) findViewById(R.id.editor_title);
			EditText editor_body = (EditText) findViewById(R.id.editor_body);

			//�^�C�g����������
			if(editor_title.getText() == null || editor_title.getText().toString().trim().length() == 0)
			{
				MainActivity.showDialog(this, "�x��", "�\�肪�����͂ł�");

			}

			// ���ʂ�ʒm����C���e���g���쐬����
			Intent result = new Intent();

			if (editorId != -1) {
				result.putExtra(MainActivity.EXTRA_ID, editorId);
			}
			result.putExtra(MainActivity.EXTRA_TITLE, editor_title.getText()
					.toString());
			result.putExtra(MainActivity.EXTRA_BODY, editor_body.getText()
					.toString());
			setResult(RESULT_OK, result);
			finish();
			break;

		default:
			break;
		}
	}

}