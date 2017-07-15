package com.example.dish.atys;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.beans.User;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

	@ViewInject(R.id.et_register_num)
	private EditText etRegisterNum;

	@ViewInject(R.id.et_register_password)
	private EditText etRegisterPassword;

	@ViewInject(R.id.et_register_rePassword)
	private EditText etRegisterRePassword;

	@ViewInject(R.id.btn_register)
	private Button btnRegister;

	private DbManager db;

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub

	}

	@Event(value = { R.id.btn_register }, type = View.OnClickListener.class)
	private void onClick(View v) {
		String RegisterNum = etRegisterNum.getText().toString();
		String RegisterPassword = etRegisterPassword.getText().toString();
		String RegisterRePassword = etRegisterRePassword.getText().toString();
		if (RegisterNum.length() < 4) {
			Toast.makeText(RegisterActivity.this, "账号不能少于4位", 0).show();
			return;
		}
		if (RegisterPassword.length() < 4) {
			Toast.makeText(RegisterActivity.this, "密码不能少于4位", 0).show();
			return;
		}
		if (!RegisterPassword.equals(RegisterRePassword)) {
			Toast.makeText(RegisterActivity.this, "两次密码不一致", 0).show();
			return;
		} else {
			db = MyApplication.getDb();
			User u = null;
			try {
				u = db.selector(User.class).where("phoneNumber", "=", RegisterNum).findFirst();
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (u != null) {
				showShortToast("手机号已经被注册");
				return;
			} else {
				User user = new User(RegisterNum, RegisterNum, RegisterPassword);

				try {
					db.save(user);
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
			showShortToast("注册成功");
			finish();

		}
	}

}
