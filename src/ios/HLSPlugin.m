//
//  HLSPlayerPlugin.m
//  SenalRadionica
//
//  Created by Jaime Caicedo on 2/3/14.
//
//

#import "HLSPlugin.h"
#import "STKAudioPlayer.h"
#import "STKAutoRecoveringHttpDataSource.h"
#import "SampleQueueId.h"
#import <Foundation/Foundation.h>

static STKAudioPlayer *audioPlayer = nil;



@implementation HLSPlugin
@synthesize mediaId;
@synthesize resourcePath;

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
    }
    
}

- (void)stopPlayingAudio:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin stopPlayinAudio");
    
    if(audioPlayer.state != STKAudioPlayerStatePaused)
    {
        NSLog(@"%@",@"Stop ");
        [audioPlayer pause];
    }
    
}

- (void)setVolume:(CDVInvokedUrlCommand*)command{
	
    
    NSString* callbackId = command.callbackId;
    
    NSString* mmediaId = [command.arguments objectAtIndex:0];
    NSNumber* volume =  [command.arguments objectAtIndex:1];
    
    NSLog(@"HLSPlayer setting Volume %@  mediaId %@", volume, mmediaId);
    
    audioPlayer.volume = [volume intValue];
    
}



@end
