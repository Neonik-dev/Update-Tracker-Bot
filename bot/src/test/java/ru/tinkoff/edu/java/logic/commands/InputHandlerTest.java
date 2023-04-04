package ru.tinkoff.edu.java.logic.commands;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.logic.commands.InputHandler;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputHandlerTest {
    @Test
    void unknownCommand_MessageUnknownCommand() {
        // given
//         Update{update_id=362941556, message=Message{message_id=978, message_thread_id=null, from=User{id=344419982, is_bot=false, first_name='Глеба', last_name='null', username='baldhoee', language_code='ru', is_premium='null', added_to_attachment_menu='null', can_join_groups=null, can_read_all_group_messages=null, supports_inline_queries=null}, sender_chat=null, date=1680608204, chat=Chat{id=344419982, type=Private, first_name='Глеба', last_name='null', is_forum=null, username='baldhoee', title='null', photo=null, active_usernames=null, emoji_status_custom_emoji_id='null', bio='null', has_private_forwards=null, has_restricted_voice_and_video_messages=null, has_hidden_members=null, has_aggressive_anti_spam_enabled=null, join_to_send_messages=null, join_by_request=null, description='null', invite_link='null', pinned_message=null, permissions=null, slow_mode_delay=null, message_auto_delete_time=null, has_protected_content=null, sticker_set_name='null', can_set_sticker_set=null, linked_chat_id=null, location=null}, forward_from=null, forward_from_chat=null, forward_from_message_id=null, forward_signature='null', forward_sender_name='null', forward_date=null, is_topic_message=null, is_automatic_forward=null, reply_to_message=null, via_bot=null, edit_date=null, has_protected_content=null, has_media_spoiler=null, media_group_id='null', author_signature='null', text='awda', entities=null, caption_entities=null, audio=null, document=null, animation=null, game=null, photo=null, sticker=null, video=null, voice=null, video_note=null, caption='null', contact=null, location=null, venue=null, poll=null, dice=null, new_chat_members=null, left_chat_member=null, new_chat_title='null', new_chat_photo=null, delete_chat_photo=null, group_chat_created=null, supergroup_chat_created=null, channel_chat_created=null, message_auto_delete_timer_changed=null, migrate_to_chat_id=null, migrate_from_chat_id=null, pinned_message=null, invoice=null, successful_payment=null, user_shared=null, chat_shared=null, connected_website='null', passport_data=null, proximity_alert_triggered=null, forum_topic_created=null, forum_topic_edited=null, forum_topic_closed=null, forum_topic_reopened=null, general_forum_topic_hidden=null, general_forum_topic_unhidden=null, write_access_allowed=null, video_chat_started=null, video_chat_ended=null, video_chat_participants_invited=null, video_chat_scheduled=null, reply_markup=null, web_app_data=null}, edited_message=null, channel_post=null, edited_channel_post=null, inline_query=null, chosen_inline_result=null, callback_query=null, shipping_query=null, pre_checkout_query=null, poll=null, poll_answer=null, my_chat_member=null, chat_member=null, chat_join_request=null}
        String answer = "Такой команды не существует.\nПопробуйте воспользоваться командой /help";
        Long chat_id = new Random().nextLong();
        String json = String.format("{message={chat={id=%d}, text=randomText}}", chat_id);
        Update update = new Gson().fromJson(json, Update.class);

        // when
        SendMessage sendMessage = new InputHandler().run(update);

        // then
        assertAll(
                () -> assertEquals(sendMessage.getParameters().get("text"), answer),
                () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id)
        );
    }
}
