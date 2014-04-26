package com.movilesseguros.dal;

import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.android.helper.database.DatabaseHelper;

public class DBManager extends DatabaseHelper {

	private static final String TAG = DBManager.class.getSimpleName();

	private static final String DB_NAME = "Consultas";
	private static final int DB_VERSION = 1;
	private static DBManager instancia;

	private DBManager(Context context) {
		super(context, DB_NAME, DB_VERSION, getDBPath(context));

	}

	public static DBManager getInstancia(Context context) {
		if (instancia == null) {
			instancia = new DBManager(context);
			try {
				instancia.createDatabaseFromFile();
				instancia.abrir();
			} catch (IOException e) {
				Log.e(TAG, "", e);
			} catch (SQLiteException e) {
				Log.e(TAG, "", e);
			}
		}

		return instancia;
	}

	@Override
	public void onDatabaseCreate(SQLiteDatabase db) {
		// do nothing

	}

	@Override
	public void onDatabaseUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		// do nothing

	}

	private static String getDBPath(Context context) {

		return context.getDatabasePath(DB_NAME).getPath();
	}

}
