package tk.burdukowsky.mpc_hc_android

enum class Command(val value: Int) {
    play(887),
    pause(888),
    stop(890),
    forward(922),
    back(921),
    increase_speed(895),
    decrease_speed(894),
    seek_forward_medium(902),
    seek_backward_medium(901),
    mute(909),
    volume_down(908),
    volume_up(907),
    fullscreen(830),
    next_audio(952),
    previous_audio(953),
    next_subtitles(954),
    previous_subtitles(955),
    next_playlist_item(920),
    previous_playlist_item(919),
    video_frame_inside(839),
    video_frame_outside(840)
}
