//
//  HLSPlayerPlugin.m
//  SenalRadionica
//
//  Created by Jaime Caicedo on 2/3/14.
//
//

#import "HLSPlugin.h"
//#import "STKAudioPlayer.h"
//#import "STKAutoRecoveringHttpDataSource.h"
//#import "SampleQueueId.h"
#import <Foundation/Foundation.h>
#import <MediaPlayer/MediaPlayer.h>

static MPMoviePlayerController *moviePlayer = nil;



@implementation HLSPlugin
@synthesize mediaId;
@synthesize resourcePath;
@synthesize moviePlayer;

- (void)create:(CDVInvokedUrlCommand*)command{
	
    
    self.mediaId = [command.arguments objectAtIndex:0];
    self.resourcePath = [command.arguments objectAtIndex:1];
    
    NSLog(@"HLSPlugin created path-> %@    id-> %@", self.resourcePath, self.mediaId);
	
	CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)getCurrentPositionAudio:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin getCurrentPosition");
}

- (void)startPlayingAudio:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin startPlayingAudio");
    
    /*
    @synchronized(self) {
        if (audioPlayer == nil) {
            //audioPlayer = [[STKAudioPlayer alloc] init];
            NSURL* url = [NSURL URLWithString:self.resourcePath];
            
            audioPlayer = [[STKAudioPlayer alloc] initWithOptions:STKAudioPlayerOptionFlushQueueOnSeek|STKAudioPlayerOptionEnableVolumeMixer];
            audioPlayer.meteringEnabled = YES;
            audioPlayer.volume = 1.0;
            
            STKDataSource* dataSource = [STKAudioPlayer dataSourceFromURL:url];
            
            [audioPlayer setDataSource:dataSource withQueueItemId:[[SampleQueueId alloc] initWithUrl:url andCount:0]];
            return;
        } else {
            if(audioPlayer.state != STKAudioPlayerStatePlaying)
            {
                NSLog(@"%@",@"Resume ");
                [audioPlayer resume];
            }
        }
    } */
    
    @synchronized(self) {
        if (moviePlayer == nil) {
            //audioPlayer = [[STKAudioPlayer alloc] init];
            NSURL* url = [NSURL URLWithString:self.resourcePath];
            
            moviePlayer = [[MPMoviePlayerController alloc] initWithContentURL:url];
            moviePlayer.shouldAutoplay = YES;
            
            [moviePlayer play];
            return;
        } else {
            if(moviePlayer.playbackState != MPMoviePlaybackStatePlaying)
            {
                NSLog(@"%@",@"Resume ");
                [moviePlayer play];
            }
        }
    }
    
}

- (void)stopPlayingAudio:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin stopPlayinAudio");
    
    /*
     if(audioPlayer.state != STKAudioPlayerStatePaused)
    {
        NSLog(@"%@",@"Stop ");
        [audioPlayer pause];
    }
     */
    
    if(moviePlayer.playbackState != MPMoviePlaybackStatePaused)
    {
        NSLog(@"%@",@"Stop ");
        [moviePlayer stop];
        moviePlayer = nil;
        
    }
    
}

- (void)setVolume:(CDVInvokedUrlCommand*)command{
	
    
    NSString* callbackId = command.callbackId;
    
    NSString* mmediaId = [command.arguments objectAtIndex:0];
    NSNumber* volume =  [command.arguments objectAtIndex:1];
    
    NSLog(@"HLSPlayer setting Volume %@  mediaId %@", volume, mmediaId);
    
    //audioPlayer.volume = [volume intValue];
    
    //[moviePlayer ]
    
    [[MPMusicPlayerController applicationMusicPlayer] setVolume:volume.floatValue];
}



@end
