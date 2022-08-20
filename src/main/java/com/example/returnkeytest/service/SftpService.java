package com.example.returnkeytest.service;

import com.example.returnkeytest.config.SftpConfig;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SftpService {
    private final SftpConfig config;

    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        ChannelSftp channelSftp = createChannelSftp();

        try {
            channelSftp.put(localFilePath, remoteFilePath);
            return true;
        } catch (Exception e) {
            log.error("Error upload file", e);
        } finally {
            disconnectChannelSftp(channelSftp);
        }

        return false;
    }

    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        ChannelSftp channelSftp = createChannelSftp();
        OutputStream outputStream;
        try {
            File file = new File(localFilePath);

            outputStream = new FileOutputStream(file);

            log.info("downloading remoteFilePath: {}", remoteFilePath);
            channelSftp.get(remoteFilePath, outputStream);

            log.info("file downloaded: {}", localFilePath);
            file.createNewFile();
            return true;
        } catch (SftpException | IOException e) {
            log.error("Error download file", e);
        } finally {
            disconnectChannelSftp(channelSftp);
        }

        return false;
    }

    private ChannelSftp createChannelSftp() {
        try {
            JSch jSch = new JSch();
            Session session = jSch.getSession(config.getUsername(), config.getHost(), config.getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(config.getPassword());
            session.connect(config.getSessionTimeout());
            Channel channel = session.openChannel("sftp");
            channel.connect(config.getChannelTimeout());
            return (ChannelSftp) channel;
        } catch (JSchException e) {
            log.error("Create ChannelSftp error", e);
        }

        return null;
    }

    private void disconnectChannelSftp(ChannelSftp channelSftp) {
        try {
            if (channelSftp == null)
                return;

            if (channelSftp.isConnected())
                channelSftp.disconnect();

            if (channelSftp.getSession() != null)
                channelSftp.getSession().disconnect();

        } catch (Exception ex) {
            log.error("SFTP disconnect error", ex);
        }
    }
}
