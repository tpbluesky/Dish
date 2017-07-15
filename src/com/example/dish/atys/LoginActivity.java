package com.example.dish.atys;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.beans.User;
import com.example.dish.utils.SPUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.btn_login)
	private Button btnLogin;

	@ViewInject(R.id.tv_register)
	private TextView tvRegister;

	@ViewInject(R.id.et_num)
	private TextView etNum;

	@ViewInject(R.id.et_password)
	private TextView etPassword;

	private DbManager db;
	private SharedPreferences sp;

	private User u = null, u1 = null;

	private SPUtils spUtils;

	@Override
	protected void initViews() {
		db = MyApplication.getDb();
		spUtils = new SPUtils(this);
		String account = spUtils.getAccount();
		String pwd = spUtils.getPassword();
		if (account != null && pwd != null) {
			try {
				User u = db.selector(User.class).where("phoneNumber", "=", account).and("pasword", "=", pwd)
						.findFirst();
				if (u != null) {
					MyApplication.setLoginUser(u);
					startActivity(new Intent(this, MainActivity.class));
					finish();
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}

	@Event(value = { R.id.btn_login, R.id.tv_register }, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			String num = etNum.getText().toString().trim();
			String password = etPassword.getText().toString().trim();
			if (num.length() < 4) {
				Toast.makeText(LoginActivity.this, "账号不能少于4位", 0).show();
				return;
			}
			if (password.length() < 4) {
				Toast.makeText(LoginActivity.this, "密码不能少于4位", 0).show();
				return;
			}
			try {
				u = db.selector(User.class).where("phoneNumber", "=", num).findFirst();
				if (u == null) {
					showShortToast("用户名不存在");
					return;
				}
				u1 = db.selector(User.class).where("phoneNumber", "=", num).and("pasword", "=", password).findFirst();
				if (u1 == null) {
					showShortToast("用户名或密码错误,请重新输入");
					etPassword.setText("");
					return;
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
			spUtils.saveAccount(num, password);
			MyApplication.setLoginUser(u1);
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
			break;
		case R.id.tv_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		default:
			break;
		}
	}

}
