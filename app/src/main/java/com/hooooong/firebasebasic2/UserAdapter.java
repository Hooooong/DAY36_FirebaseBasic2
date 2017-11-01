package com.hooooong.firebasebasic2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hooooong.firebasebasic2.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Hong on 2017-10-31.
 */


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList = new ArrayList<>();
    private Callback callback;

    public UserAdapter(Callback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.setTextId(user.getId());
        holder.setTextEmail(user.getEmail());
        /*holder.setTextToken(user.getToken());*/
        holder.setToekn(user.getToken());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void modifyUserData(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textId, textEmail;
        private String token;

        ViewHolder(View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.textId);
            textEmail = itemView.findViewById(R.id.textEmail);
            //textToken = itemView.findViewById(R.id.textToken);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = textId.getText().toString();
                    callback.setUser(textEmail.getText().toString(),token);
                    Toast.makeText(v.getContext(), "ID : " + id +", Token : " + token, Toast.LENGTH_SHORT).show();
                }
            });
        }

        void setTextId(String id) {
            textId.setText(id);

        }

        void setTextEmail(String email){
            textEmail.setText(email);
        }

        void setToekn(String token){
            this.token = token;
        }

        /*void setTextToken(String token) {
            textToken.setText(token);
        }*/
    }


    public interface Callback{
        void setUser(String email, String token);
    }
}
